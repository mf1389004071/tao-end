#!/usr/bin/env bash
# ============================================
# bt10 前后端构建脚本（参考 Windows bat 交互方式）
# 脚本位于 tao-end/，会联动同级目录 tao-pc、tao-app
#
# 启动时支持选项：
#   1  编译 tao-end（Maven install，全模块）
#   2  打包 tao-end 为可独立运行的 fat jar，并打开产物目录
#   3  编译 tao-pc，将 dist 打成 zip，并打开 zip 所在目录
#   4  编译 tao-app 为微信小程序发布包，并打开产物目录
#   5  顺序执行 1 → 4
#   6  编译并打包 tao-end：顺序执行 1、2，并打开 geek-admin/target
#   7  编译并打包「管理端」：顺序执行 1、2、3（后端 + tao-pc 压缩包）
#
# 用法示例：
#   ./build.sh                    # 交互选菜单（默认跳过测试 + 默认 JDK，见下）
#   ./build.sh 2                  # 直接执行选项 2
#   ./build.sh 5 --run-tests      # 全流程且 Maven 执行单元测试
#
# 默认值（可被参数或环境变量覆盖）：
#   - Maven：默认 -DskipTests（跳过测试）；用 --run-tests 或 BT10_RUN_TESTS=1 改为执行测试
#   - JAVA_HOME：默认 DEFAULT_JAVA_HOME（脚本内常量）；用 --java-home= 或 BT10_JAVA_HOME 覆盖
#
# 指定其它 JDK（pom 要求 Java 21）：
#   ./build.sh 2 --java-home=/path/to/jdk-21
#   BT10_JAVA_HOME=/path/to/jdk-21 ./build.sh 6
# ============================================
set -o pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TAO_END="$SCRIPT_DIR"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
TAO_PC="$REPO_ROOT/tao-pc"
TAO_APP="$REPO_ROOT/tao-app"
GEEK_ADMIN_TARGET="$TAO_END/geek-admin/target"
JAR_NAME="geek-admin.jar"
MP_WEIXIN_OUT="$TAO_APP/dist/build/mp-weixin"

export MAVEN_OPTS="${MAVEN_OPTS:--Xmx2048m}"

# 本机默认 JDK（未传 --java-home、未设置 BT10_JAVA_HOME 时使用；无效则降级用 PATH）
DEFAULT_JAVA_HOME="/Users/msc/Library/Java/JavaVirtualMachines/ms-21.0.10/Contents/Home"

# 统一解析：菜单数字留在 REMAIN_ARGS；JDK / 测试开关单独处理
REMAIN_ARGS=()
# 默认跳过 Maven 测试；BT10_RUN_TESTS=1 或命令行 --run-tests 可改为执行测试
SKIP_TESTS_ARGS=(-DskipTests)
if [[ "${BT10_RUN_TESTS:-0}" == "1" ]] || [[ "${BT10_RUN_TESTS:-}" =~ ^[tT]rue$ ]]; then
  SKIP_TESTS_ARGS=()
fi
JAVA_HOME_OVERRIDE=""
for arg in "$@"; do
  case "$arg" in
    --java-home=*)
      JAVA_HOME_OVERRIDE="${arg#*=}"
      ;;
    --run-tests|--no-skip-tests|-runTests)
      SKIP_TESTS_ARGS=()
      ;;
    -skipTests|--skipTests|-DskipTests)
      SKIP_TESTS_ARGS=(-DskipTests)
      ;;
    *)
      REMAIN_ARGS+=("$arg")
      ;;
  esac
done

apply_java_home() {
  local jh="${JAVA_HOME_OVERRIDE:-}"
  if [[ -z "$jh" ]] && [[ -n "${BT10_JAVA_HOME:-}" ]]; then
    jh="${BT10_JAVA_HOME}"
  fi
  if [[ -z "$jh" ]]; then
    jh="$DEFAULT_JAVA_HOME"
  fi
  if [[ -z "$jh" ]]; then
    return 0
  fi
  # 展开 ~，并尽量规范为绝对路径
  jh="${jh/#\~/$HOME}"
  if [[ -d "$jh" ]]; then
    jh="$(cd "$jh" && pwd)"
  fi
  if [[ ! -x "$jh/bin/java" ]]; then
    warn "JAVA_HOME 未应用（路径无效或缺少 bin/java）：$jh — 继续使用 PATH 中的 java"
    return 0
  fi
  export JAVA_HOME="$jh"
  export PATH="$JAVA_HOME/bin:$PATH"
  info "Maven 将使用 JAVA_HOME=$JAVA_HOME"
  info "java 路径: $(command -v java 2>/dev/null || echo unknown)"
  java -version 2>&1 | head -n 1 || true
}

die() {
  echo "[ERROR] $*" >&2
  exit 1
}

info() { echo "[INFO] $*"; }
warn() { echo "[WARN] $*"; }

open_dir() {
  local d="$1"
  [[ -d "$d" ]] || return 0
  case "$(uname -s)" in
    Darwin*) open "$d" ;;
    MINGW*|MSYS*|CYGWIN*)
      if command -v cygpath >/dev/null 2>&1; then
        explorer "$(cygpath -w "$d")" 2>/dev/null || true
      else
        explorer "$d" 2>/dev/null || true
      fi
      ;;
    *)
      xdg-open "$d" 2>/dev/null || true
      ;;
  esac
}

ensure_mvn() {
  command -v mvn >/dev/null 2>&1 || die "未找到 mvn，请安装 Maven 并加入 PATH"
}

ensure_node() {
  command -v node >/dev/null 2>&1 || die "未找到 node，请安装 Node.js"
  if command -v yarn >/dev/null 2>&1; then
    echo yarn
  elif command -v npm >/dev/null 2>&1; then
    echo npm
  else
    die "未找到 yarn 或 npm"
  fi
}

run_frontend_install_build() {
  local dir="$1"
  shift
  local pm
  pm="$(ensure_node)"
  (
    cd "$dir" || exit 1
    if [[ "$pm" == yarn ]]; then
      yarn install
    else
      npm install
    fi
  ) || return 1
  (
    cd "$dir" || exit 1
    if [[ "$pm" == yarn ]]; then
      yarn "$@"
    else
      npm run "$@"
    fi
  )
}

# --- 1 编译 tao-end ---
do_maven_install() {
  ensure_mvn
  if ((${#SKIP_TESTS_ARGS[@]})); then
    info "Maven：tao-end 全模块 clean install（跳过测试）"
  else
    info "Maven：tao-end 全模块 clean install（运行测试）"
  fi
  (cd "$TAO_END" && mvn clean install "${SKIP_TESTS_ARGS[@]}")
}

# --- 2 打包 fat jar ---
do_maven_package_jar() {
  ensure_mvn
  info "Maven：仅构建 geek-admin 及依赖（-pl geek-admin -am），生成可执行 jar"
  (cd "$TAO_END" && mvn -pl geek-admin -am clean package "${SKIP_TESTS_ARGS[@]}") || return 1
  local jar="$GEEK_ADMIN_TARGET/$JAR_NAME"
  [[ -f "$jar" ]] || die "未找到可执行 jar：$jar（请检查 geek-admin 是否成功 repackage）"
  info "可执行 jar：$jar"
}

# --- 3 tao-pc：build + zip dist ---
do_pc_build_zip() {
  [[ -d "$TAO_PC" ]] || die "目录不存在：$TAO_PC"
  local ts zip_name zip_dir
  ts="$(date +%Y-%m-%d-%H%M%S)"
  zip_dir="$REPO_ROOT"
  zip_name="tao-pc-dist-${ts}.zip"
  info "前端目录：$TAO_PC"
  if [[ -d "$TAO_PC/dist" ]]; then
    info "删除已有 dist..."
    rm -rf "$TAO_PC/dist"
  fi
  run_frontend_install_build "$TAO_PC" "build:prod" || die "tao-pc 构建失败"
  [[ -d "$TAO_PC/dist" ]] || die "构建后未找到 dist 目录"
  (
    cd "$TAO_PC" || exit 1
    zip -r -q "$zip_dir/$zip_name" dist
  ) || die "打包 zip 失败"
  info "已生成：$zip_dir/$zip_name"
  open_dir "$zip_dir"
}

# --- 4 tao-app：微信小程序发布包 ---
do_app_mp_weixin() {
  [[ -d "$TAO_APP" ]] || die "目录不存在：$TAO_APP"
  info "小程序目录：$TAO_APP"
  if [[ -d "$TAO_APP/dist" ]]; then
    info "删除已有 dist..."
    rm -rf "$TAO_APP/dist"
  fi
  run_frontend_install_build "$TAO_APP" "build:mp-weixin" || die "tao-app 微信小程序构建失败"
  [[ -d "$MP_WEIXIN_OUT" ]] || die "未找到发布包目录：$MP_WEIXIN_OUT（请确认 uni 构建输出路径）"
  info "微信小程序发布包：$MP_WEIXIN_OUT"
  open_dir "$MP_WEIXIN_OUT"
}

print_menu() {
  echo "============================================"
  echo "  bt10 构建脚本"
  echo "============================================"
  echo "请选择："
  echo "  1  仅编译 tao-end（mvn clean install）"
  echo "  2  仅打包 tao-end（可执行 fat jar），并打开 target 目录"
  echo "  3  仅编译并压缩 tao-pc（dist → zip），并打开 zip 所在目录"
  echo "  4  仅编译 tao-app（微信小程序），并打开发布包目录"
  echo "  5  顺序执行 1 → 4"
  echo "  6  编译并打包 tao-end：顺序 1 → 2，并打开 target 目录"
  echo "  7  编译并打包管理端：顺序 1 → 2 → 3（后端 jar + tao-pc zip）"
  echo ""
  echo "默认值：Maven 跳过测试；-DskipTests；JAVA_HOME=$DEFAULT_JAVA_HOME（若存在）"
  echo "附加参数（覆盖默认）："
  echo "  --run-tests / --no-skip-tests / -runTests   执行 Maven 单元测试（取消默认跳过）"
  echo "  -skipTests / --skipTests / -DskipTests     显式跳过测试（与默认一致，可省略）"
  echo "  环境 BT10_RUN_TESTS=1                       同 --run-tests"
  echo "  --java-home=/绝对路径/jdk                   指定 JDK（覆盖脚本内 DEFAULT_JAVA_HOME）"
  echo "  环境 BT10_JAVA_HOME=...                     同上，优先级低于 --java-home="
  echo "模拟生产运行"
  echo 'export JAVA_HOME=/Users/msc/Library/Java/JavaVirtualMachines/ms-21.0.10/Contents/Home && "$JAVA_HOME/bin/java" -Xmx512M -jar /Users/msc/workings/workspace/demo/bt10/tao-end/geek-admin/target/geek-admin.jar --spring.profiles.active=prod --server.port=19999'
  echo "============================================"
}

resolve_choice() {
  local c="${1:-}"
  if [[ -n "$c" ]]; then
    echo "$c"
    return
  fi
  # 菜单必须写到 stderr：main 里 choice="$(resolve_choice ...)" 会吞掉 stdout，
  # 否则菜单被捕获进变量，终端上只剩 read -p（stderr）的提示。
  print_menu >&2
  read -r -p "请输入选项 (1-7): " c || true
  echo "$c"
}

main() {
  local raw_choice choice
  raw_choice=""
  for arg in "$@"; do
    [[ "$arg" =~ ^[1-7]$ ]] && raw_choice="$arg"
  done
  choice="$(resolve_choice "$raw_choice")"
  choice="$(echo "$choice" | tr -d '[:space:]')"
  if [[ ! "$choice" =~ ^[1-7]$ ]]; then
    choice=""
  fi

  if [[ -z "$choice" ]]; then
    info "未选择有效选项，退出。"
    exit 0
  fi

  echo ""
  echo "============================================"
  echo "  已选：$choice"
  echo "============================================"
  if ((${#SKIP_TESTS_ARGS[@]})); then
    echo "  Maven 将跳过测试"
  fi
  echo ""

  case "$choice" in
    1) do_maven_install ;;
    2)
      do_maven_package_jar
      open_dir "$GEEK_ADMIN_TARGET"
      ;;
    3) do_pc_build_zip ;;
    4) do_app_mp_weixin ;;
    5)
      do_maven_install || die "步骤 1 失败"
      do_maven_package_jar || die "步骤 2 失败"
      do_pc_build_zip || die "步骤 3 失败"
      do_app_mp_weixin || die "步骤 4 失败"
      info "全流程（1-4）已完成"
      ;;
    6)
      do_maven_install || die "步骤 1 失败"
      do_maven_package_jar || die "步骤 2 失败"
      open_dir "$GEEK_ADMIN_TARGET"
      info "tao-end 编译并打包完成（1 → 2）"
      ;;
    7)
      do_maven_install || die "步骤 1 失败"
      do_maven_package_jar || die "步骤 2 失败"
      do_pc_build_zip || die "步骤 3 失败"
      info "管理端（后端 + tao-pc）编译并打包完成（1 → 2 → 3）"
      ;;
    *) die "无效选项: $choice" ;;
  esac

  echo ""
  echo "============================================"
  echo "  操作结束"
  echo "============================================"
}

apply_java_home
main "${REMAIN_ARGS[@]}"
