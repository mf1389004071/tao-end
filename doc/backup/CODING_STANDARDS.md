# Better10 项目编码开发规范

本文档定义了 Better10 项目的全方位编码开发规范，确保代码质量、一致性和可维护性。

## 📋 目录

- [总体原则](#总体原则)
- [后端开发规范](#后端开发规范)
- [前端开发规范](#前端开发规范)
- [数据库规范](#数据库规范)
- [API 设计规范](#api-设计规范)
- [代码风格](#代码风格)
- [测试规范](#测试规范)
- [Git 提交规范](#git-提交规范)

---

## 总体原则

### 1. AI-Native 代码风格

- **单一职责原则 (SRP)**: 每个类/函数只做一件事
- **强类型约束**: 使用类型提示和 DTO 确保类型安全
- **清晰的命名**: 使用描述性的变量和函数名，便于 AI 理解上下文
- **注释优先**: 复杂逻辑必须添加注释说明

### 2. 架构模式

- **Service Action 模式**: 所有业务逻辑封装在 `app/Actions/` 中
- **DTO 数据契约**: 使用 `spatie/laravel-data` 定义数据传输对象
- **Repository 模式**: 复杂查询逻辑封装在 Repository 中（如需要）

### 3. 代码复用

- **优先使用已有组件**: 开发前先检查是否有可复用的组件或方法
- **避免重复代码**: 提取公共逻辑到工具类或 Trait
- **遵循 DRY 原则**: Don't Repeat Yourself

---

## 后端开发规范

### 1. 目录结构

```
app/
├── Actions/              # 业务逻辑原子层
│   ├── Auth/            # 认证相关
│   ├── OneID/           # 用户管理相关
│   ├── Drive/           # 文件管理相关
│   └── Event/           # 活动管理相关
├── Data/                # DTO 数据契约层
│   ├── Auth/
│   └── OneID/
├── Http/
│   ├── Controllers/     # 控制器（仅处理 HTTP 请求）
│   └── Middleware/      # 中间件
├── Models/              # Eloquent 模型
├── Enums/               # 枚举类型
├── Events/              # 领域事件
├── Jobs/                # 异步任务
└── Services/            # 外部集成层
```

### 2. Action 类规范

**命名规范**:
- 类名必须以动词开头，以 `Action` 结尾
- 例如: `CreateUserAction`, `UpdateUserAction`, `DeleteUserAction`

**结构规范**:
```php
<?php

namespace App\Actions\OneID;

use App\Models\User;
use App\Data\OneID\CreateUserDto;

class CreateUserAction
{
    /**
     * 创建用户
     *
     * @param CreateUserDto $data 用户数据
     * @return User 创建的用户实例
     */
    public function handle(CreateUserDto $data): User
    {
        // 业务逻辑实现
        return User::create([...]);
    }
}
```

**规则**:
- ✅ 只有一个 `handle()` 公有方法
- ✅ 方法参数使用 DTO 类型
- ✅ 返回值类型明确
- ✅ 不直接处理 HTTP 请求
- ✅ 不直接访问 `Request` 对象

### 3. DTO 规范

**命名规范**:
- 类名以功能命名，以 `Dto` 结尾
- 例如: `CreateUserDto`, `UpdateUserDto`, `LoginDto`

**结构规范**:
```php
<?php

namespace App\Data\OneID;

use Spatie\LaravelData\Data;
use Spatie\LaravelData\Attributes\Validation\Required;
use Spatie\LaravelData\Attributes\Validation\Regex;

class CreateUserDto extends Data
{
    public function __construct(
        #[Required]
        public string $name,
        
        #[Required]
        #[Regex('/^1[3-9]\d{9}$/', message: '手机号格式不正确')]
        public string $mobile,
        
        #[Required]
        public string $password,
    ) {}
}
```

**规则**:
- ✅ 使用 `spatie/laravel-data` 包
- ✅ 属性使用强类型
- ✅ 使用 Validation 属性注解
- ✅ 提供清晰的错误消息

### 4. Controller 规范

**命名规范**:
- 类名使用单数形式，以 `Controller` 结尾
- 例如: `UserController`, `AuthController`

**结构规范**:
```php
<?php

namespace App\Http\Controllers\OneID;

use App\Http\Controllers\Controller;
use App\Actions\OneID\CreateUserAction;
use App\Data\OneID\CreateUserDto;
use Illuminate\Http\Request;
use Inertia\Inertia;

class UserController extends Controller
{
    public function store(Request $request, CreateUserAction $createUserAction)
    {
        // 1. 验证请求数据
        $data = CreateUserDto::from($request->validate([...]));
        
        // 2. 调用 Action
        $user = $createUserAction->handle($data);
        
        // 3. 返回响应
        return redirect()->route('users.index')
            ->with('success', '用户创建成功');
    }
}
```

**规则**:
- ✅ 控制器只处理 HTTP 请求/响应
- ✅ 业务逻辑委托给 Action
- ✅ 使用 DTO 进行数据验证
- ✅ 返回 Inertia 响应或重定向

### 5. Model 规范

**命名规范**:
- 使用单数形式，首字母大写
- 例如: `User`, `Department`, `File`

**结构规范**:
```php
<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\SoftDeletes;

class User extends Model
{
    use HasUuids, SoftDeletes;

    protected $fillable = [
        'name',
        'mobile',
        'password',
    ];

    protected $casts = [
        'status' => UserStatus::class,
    ];

    public function department(): BelongsTo
    {
        return $this->belongsTo(Department::class);
    }
}
```

**规则**:
- ✅ 使用 UUID v7 主键（`HasUuids`）
- ✅ 使用软删除（`SoftDeletes`）
- ✅ 定义 `$fillable` 字段
- ✅ 使用 Enum 类型转换
- ✅ 定义关联关系方法

### 6. Enum 规范

**命名规范**:
- 使用单数形式，描述状态或类型
- 例如: `UserStatus`, `EventStatus`, `AiTaskProvider`

**结构规范**:
```php
<?php

namespace App\Enums;

enum UserStatus: string
{
    case PENDING = 'PENDING';
    case ACTIVE = 'ACTIVE';
    case SUSPENDED = 'SUSPENDED';
    case OFFBOARDED = 'OFFBOARDED';
}
```

**规则**:
- ✅ 使用 PHP 8.1+ 的 Enum 语法
- ✅ 值使用大写字符串
- ✅ 在 Model 中使用类型转换

---

## 前端开发规范

### 1. 目录结构

```
resources/js/
├── Pages/                # 页面组件
│   ├── Auth/
│   ├── OneID/
│   └── Dashboard.vue
├── Layouts/             # 布局组件
│   └── AppLayout.vue
├── Components/          # 可复用组件
├── Composables/         # 组合式函数
└── Utils/               # 工具函数
```

### 2. 组件命名规范

**文件命名**:
- 使用 PascalCase
- 例如: `UserList.vue`, `ChangePasswordModal.vue`

**组件命名**:
- 在 `<script setup>` 中无需定义组件名
- 在模板中使用 kebab-case
- 例如: `<user-list />`, `<change-password-modal />`

### 3. Vue 组件结构

**标准结构**:
```vue
<template>
    <!-- 模板内容 -->
</template>

<script setup>
import { ref, computed } from 'vue';
import { useForm } from '@inertiajs/vue3';
import AppLayout from '@/Layouts/AppLayout.vue';

// Props
const props = defineProps({
    user: Object,
    errors: Object,
});

// Emits
const emit = defineEmits(['close', 'success']);

// 响应式数据
const form = useForm({
    name: '',
    mobile: '',
});

// 方法
const submit = () => {
    form.post(route('users.store'));
};
</script>

<style scoped>
/* 组件样式 */
</style>
```

**规则**:
- ✅ 使用 `<script setup>` 语法
- ✅ 使用 Composition API
- ✅ Props 和 Emits 使用 `defineProps` 和 `defineEmits`
- ✅ 使用 Inertia 的 `useForm` 处理表单
- ✅ 样式使用 `scoped` 或 TailwindCSS 类

### 4. 样式规范

**优先使用 TailwindCSS**:
```vue
<template>
    <div class="card p-6">
        <button class="btn btn-primary">提交</button>
    </div>
</template>
```

**自定义样式**:
- 在 `resources/css/app.css` 中定义全局样式类
- 使用 `@layer components` 定义可复用组件样式

**规则**:
- ✅ 优先使用 TailwindCSS 工具类
- ✅ 避免内联样式
- ✅ 复杂样式提取到 CSS 文件
- ✅ 使用语义化的类名

### 5. 状态管理

**使用 Inertia 的共享数据**:
```javascript
// 在 HandleInertiaRequests 中间件中共享
'auth' => [
    'user' => $request->user(),
],
```

**组件内状态**:
- 使用 `ref()` 定义响应式数据
- 使用 `computed()` 定义计算属性
- 使用 `useForm()` 处理表单状态

**规则**:
- ✅ 简单状态使用组件内 `ref`
- ✅ 表单状态使用 Inertia 的 `useForm`
- ✅ 全局状态通过 Inertia 共享
- ✅ 避免使用 Vuex/Pinia（Inertia 已提供状态管理）

### 6. 路由使用

**使用 Ziggy 路由**:
```vue
<template>
    <a :href="route('users.index')">用户列表</a>
</template>

<script setup>
import { router } from '@inertiajs/vue3';

// 编程式导航
router.visit(route('users.create'));
</script>
```

**规则**:
- ✅ 使用 `route()` 函数生成路由
- ✅ 使用 `router.visit()` 进行导航
- ✅ 保持路由名称与 Laravel 路由一致

---

## 数据库规范

### 1. 迁移文件规范

**命名规范**:
- 格式: `YYYY_MM_DD_HHMMSS_description.php`
- 例如: `2024_01_01_000001_create_users_table.php`

**结构规范**:
```php
<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('users', function (Blueprint $table) {
            $table->universalTable(); // 使用标准宏
            $table->string('name');
            $table->string('mobile')->unique();
            // ...
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('users');
    }
};
```

**规则**:
- ✅ 使用 `universalTable()` 宏（包含标准字段）
- ✅ 主键使用 UUID v7（通过宏定义）
- ✅ 外键使用 `uuid()` 类型
- ✅ 添加必要的索引
- ✅ 实现 `down()` 方法

### 2. 表设计规范

**标准字段**（通过 `universalTable()` 宏自动添加）:
- `id`: UUID v7 主键
- `created_by`, `updated_by`: 审计字段
- `created_at`, `updated_at`: 时间戳
- `deleted_at`, `deleted_by`: 软删除字段
- `text1`, `text2`, `text3`: 扩展文本字段
- `json_data`: JSONB 扩展数据字段

**规则**:
- ✅ 所有业务表使用 `universalTable()` 宏
- ✅ 主键使用 UUID v7（除特殊表如 `ai_vectors`）
- ✅ 外键使用 `uuid()` 类型
- ✅ 使用软删除（`softDeletesWithActor`）
- ✅ 添加必要的索引

### 3. 查询规范

**使用 Eloquent ORM**:
```php
// ✅ 推荐
$users = User::with('department')
    ->where('status', UserStatus::ACTIVE)
    ->paginate(15);

// ❌ 避免
$users = DB::table('users')->where('status', 'ACTIVE')->get();
```

**规则**:
- ✅ 优先使用 Eloquent ORM
- ✅ 使用关联预加载（`with()`）避免 N+1 问题
- ✅ 使用查询作用域（Scope）封装复杂查询
- ✅ 使用分页（`paginate()`）处理大量数据

---

## API 设计规范

### 1. RESTful 路由

**标准路由**:
```php
Route::prefix('users')->name('users.')->group(function () {
    Route::get('/', [UserController::class, 'index'])->name('index');
    Route::get('/create', [UserController::class, 'create'])->name('create');
    Route::post('/', [UserController::class, 'store'])->name('store');
    Route::get('/{user}', [UserController::class, 'show'])->name('show');
    Route::get('/{user}/edit', [UserController::class, 'edit'])->name('edit');
    Route::put('/{user}', [UserController::class, 'update'])->name('update');
    Route::delete('/{user}', [UserController::class, 'destroy'])->name('destroy');
});
```

**规则**:
- ✅ 遵循 RESTful 规范
- ✅ 使用资源路由（Resource Routes）
- ✅ 路由名称使用点号分隔（`users.index`）
- ✅ 使用路由前缀组织相关路由

### 2. 响应格式

**Inertia 响应**:
```php
return Inertia::render('Users/Index', [
    'users' => $users,
    'filters' => $request->only(['search', 'status']),
]);
```

**JSON 响应**（API 接口）:
```php
return response()->json([
    'success' => true,
    'data' => $user,
    'message' => '用户创建成功',
]);
```

**规则**:
- ✅ 前端页面使用 Inertia 响应
- ✅ API 接口使用 JSON 响应
- ✅ 统一错误响应格式
- ✅ 使用 Flash 消息传递成功/错误信息

---

## 代码风格

### 1. PHP 代码风格

**遵循 PSR-12 标准**:
- 使用 4 个空格缩进
- 使用 PSR-4 自动加载
- 类名使用 PascalCase
- 方法名使用 camelCase

**使用 Laravel Pint**:
```bash
./vendor/bin/pint
```

### 2. JavaScript 代码风格

**遵循 ESLint 规则**:
- 使用 2 个空格缩进
- 使用单引号
- 使用分号
- 使用 const/let，避免 var

### 3. 注释规范

**类注释**:
```php
/**
 * 用户管理控制器
 *
 * 处理用户相关的 HTTP 请求
 */
class UserController extends Controller
{
}
```

**方法注释**:
```php
/**
 * 创建用户
 *
 * @param CreateUserDto $data 用户数据
 * @return User 创建的用户实例
 */
public function handle(CreateUserDto $data): User
{
}
```

**规则**:
- ✅ 复杂逻辑必须添加注释
- ✅ 公共方法必须添加 PHPDoc
- ✅ 使用清晰的注释说明业务逻辑

---

## 测试规范

### 1. 测试框架

使用 **Pest PHP** 进行测试。

### 2. 测试文件结构

```
tests/
├── Feature/
│   ├── Auth/
│   └── OneID/
└── Unit/
    └── Actions/
```

### 3. 测试示例

```php
<?php

use App\Actions\OneID\CreateUserAction;
use App\Data\OneID\CreateUserDto;
use App\Models\User;

test('create user action creates user', function () {
    // Arrange
    $data = CreateUserDto::from([
        'name' => '测试用户',
        'mobile' => '13800138000',
        'password' => 'password123',
        'department_id' => Department::factory()->create()->id,
    ]);

    // Act
    $user = app(CreateUserAction::class)->handle($data);

    // Assert
    expect($user)->toBeInstanceOf(User::class)
        ->and($user->name)->toBe('测试用户')
        ->and($user->mobile)->toBe('13800138000');
});
```

**规则**:
- ✅ 测试 Action 类（业务逻辑）
- ✅ 使用 Arrange-Act-Assert 模式
- ✅ 测试覆盖率目标: 80%+
- ✅ 测试名称清晰描述测试内容

---

## Git 提交规范

### 1. 提交消息格式

```
<type>(<scope>): <subject>

<body>

<footer>
```

### 2. Type 类型

- `feat`: 新功能
- `fix`: 修复 bug
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建/工具相关

### 3. 示例

```
feat(OneID): 添加用户管理功能

- 实现用户列表、新增、编辑、删除功能
- 添加用户状态切换功能
- 实现用户密码修改功能

Closes #123
```

**规则**:
- ✅ 提交消息使用中文
- ✅ 第一行不超过 50 个字符
- ✅ 使用动词开头（添加、修复、更新等）
- ✅ 详细说明在 body 中

---

## 总结

遵循以上规范可以确保：

1. **代码一致性**: 团队成员遵循相同的编码标准
2. **可维护性**: 清晰的代码结构便于维护和扩展
3. **AI 友好**: 规范的代码便于 AI 理解和生成
4. **质量保证**: 通过测试和代码审查保证代码质量

**重要提示**: 
- 开发前先检查是否有可复用的组件或方法
- 遇到不确定的情况，参考本文档或咨询团队
- 定期更新本文档以反映项目的最新实践

---

## PC端与移动端分离规范

### 1. 目录结构分离

**PC端与移动端完全分离**:

```
resources/js/
├── Pages/
│   ├── PC/                # PC端页面（独立目录）
│   │   ├── Auth/          # PC端认证页面
│   │   ├── Dashboard/     # PC端Dashboard
│   │   └── OneID/         # PC端用户管理
│   └── Mobile/            # 移动端页面（独立目录）
│       ├── Auth/          # 移动端认证（登录、注册）
│       ├── Dashboard/     # 移动端首页
│       ├── Activity/      # 活动页面
│       ├── Course/        # 课程页面
│       └── Profile/       # 我的页面
├── Layouts/
│   ├── PC/                # PC端布局（独立目录）
│   │   └── AppLayout.vue
│   └── Mobile/            # 移动端布局（独立目录）
│       └── MobileLayout.vue
```

**后端控制器分离**:

```
app/Http/Controllers/
├── PC/                    # PC端控制器（独立目录）
│   ├── Auth/              # PC端认证控制器
│   ├── DashboardController.php
│   └── OneID/             # PC端用户管理
└── Mobile/                # 移动端控制器（独立目录）
    ├── Auth/              # 移动端认证控制器
    ├── MobileDashboardController.php
    ├── MobileActivityController.php
    ├── MobileCourseController.php
    └── MobileProfileController.php
```

### 2. 路由前缀分离

**PC端路由**: 使用 `/pc` 前缀
```php
Route::prefix('pc')->name('pc.')->group(function () {
    Route::get('/login', ...)->name('login');
    Route::get('/dashboard', ...)->name('dashboard');
});
```

**移动端路由**: 使用 `/m` 前缀
```php
Route::prefix('m')->name('mobile.')->group(function () {
    Route::get('/login', ...)->name('login');
    Route::get('/dashboard', ...)->name('dashboard');
});
```

**规则**:
- ✅ PC端所有路由使用 `pc.` 前缀，URL使用 `/pc` 前缀
- ✅ 移动端所有路由使用 `mobile.` 前缀，URL使用 `/m` 前缀
- ✅ 路由名称清晰区分PC端和移动端
- ✅ 便于未来实现不同端的菜单和权限控制

### 3. PC端布局规范

**顶部导航栏和侧边栏**:
- 顶部导航栏: 固定顶部，显示Logo和用户信息下拉菜单
- 侧边栏: 2层菜单结构，支持折叠展开
- 多Tab标签页: 支持打开多个页面标签，可切换和关闭

**布局结构**:
```vue
<template>
    <div class="min-h-screen bg-gray-50">
        <header>...</header>
        <div class="flex">
            <aside>...</aside>
            <main>...</main>
        </div>
    </div>
</template>
```

**规则**:
- ✅ 使用 `AppLayout` 布局组件
- ✅ Inertia渲染路径: `PC/Module/Page`
- ✅ 路由名称使用 `pc.` 前缀

### 4. 移动端布局规范

**底部导航栏**:
- 固定在页面底部
- 高度: 64px (h-16)
- 使用 `fixed bottom-0` 定位
- 适配iOS安全区域 (`safe-area-inset-bottom`)
- 4个导航项：首页、活动、课程、我的

**布局结构**:
```vue
<template>
    <div class="min-h-screen bg-gray-50 pb-20">
        <main>
            <slot />
        </main>
        <nav class="fixed bottom-0 ...">
            <!-- 底部导航 -->
        </nav>
    </div>
</template>
```

**规则**:
- ✅ 主内容区使用 `pb-20` 避免被底部导航遮挡
- ✅ 使用响应式设计，适配不同屏幕尺寸
- ✅ 导航项使用图标+文字形式
- ✅ 当前激活项高亮显示

### 5. PC端UI设计规范

**颜色规范**:
- 主色调: `primary-600` (蓝色)
- 背景色: `gray-50` (浅灰)
- 卡片背景: `white`
- 文字颜色: `gray-900` (主要), `gray-600` (次要)

**间距规范**:
- 页面内边距: `p-6` (24px)
- 卡片间距: `space-y-6` (24px)
- 元素间距: `gap-6` (24px)

**字体规范**:
- 标题: `text-2xl font-bold` (24px)
- 副标题: `text-lg font-semibold` (18px)
- 正文: `text-base` (16px)
- 辅助文字: `text-sm` (14px)

### 6. 移动端UI设计规范

**颜色规范**:
- 主色调: `primary-500` 到 `primary-600` 渐变
- 背景色: `gray-50` (浅灰)
- 卡片背景: `white`
- 文字颜色: `gray-900` (主要), `gray-600` (次要)

**间距规范**:
- 页面内边距: `p-4` (16px)
- 卡片间距: `space-y-4` (16px)
- 元素间距: `gap-4` (16px)

**圆角规范**:
- 卡片: `rounded-xl` (12px)
- 按钮: `rounded-lg` (8px)
- 头像: `rounded-full`

**字体规范**:
- 标题: `text-xl font-bold` (20px)
- 副标题: `text-lg font-semibold` (18px)
- 正文: `text-base` (16px)
- 辅助文字: `text-sm` (14px)

### 7. PC端组件规范

**页面组件命名**:
- 使用 PascalCase
- 例如: `Login.vue`, `Dashboard/Index.vue`

**组件结构**:
```vue
<template>
    <AppLayout title="页面标题">
        <!-- 页面内容 -->
    </AppLayout>
</template>

<script setup>
import AppLayout from '@/Layouts/PC/AppLayout.vue';

defineProps({
    // Props定义
});
</script>
```

**规则**:
- ✅ 所有PC端页面使用 `AppLayout` (位于 `Layouts/PC/`)
- ✅ 使用 `<script setup>` 语法
- ✅ Props 使用 `defineProps`
- ✅ 样式使用 TailwindCSS 类
- ✅ Inertia渲染路径: `PC/Module/Page`

### 8. 移动端组件规范

**页面组件命名**:
- 使用 PascalCase
- 例如: `Login.vue`, `Dashboard/Index.vue`

**组件结构**:
```vue
<template>
    <MobileLayout title="页面标题">
        <!-- 页面内容 -->
    </MobileLayout>
</template>

<script setup>
import MobileLayout from '@/Layouts/Mobile/MobileLayout.vue';

defineProps({
    // Props定义
});
</script>
```

**规则**:
- ✅ 所有移动端页面使用 `MobileLayout`
- ✅ 使用 `<script setup>` 语法
- ✅ Props 使用 `defineProps`
- ✅ 样式使用 TailwindCSS 类

### 9. PC端交互规范

**菜单交互**:
- 一级菜单可折叠展开
- 二级菜单点击打开新Tab
- 当前激活菜单高亮显示

**Tab交互**:
- 点击菜单项打开新Tab
- Tab可切换和关闭
- 至少保留一个Tab

**列表交互**:
- 列表项使用表格样式
- 支持搜索和筛选
- 分页显示

### 10. 移动端交互规范

**表单交互**:
- 输入框使用大尺寸 (`py-3`)
- 按钮使用全宽 (`w-full`)
- 提交按钮使用主色调
- 错误提示显示在输入框下方

**导航交互**:
- 点击导航项切换页面
- 当前页面导航项高亮
- 使用 Inertia.js 进行页面切换（无刷新）

**列表交互**:
- 列表项使用卡片样式
- 点击区域足够大（至少44x44px）
- 使用右箭头图标表示可点击

### 11. PC端响应式规范

**断点使用**:
- 主要适配桌面端 (1024px+)
- 使用 TailwindCSS 响应式类
- 侧边栏在小屏幕可折叠

### 12. 移动端响应式规范

**断点使用**:
- 移动端优先设计
- 使用 TailwindCSS 默认断点
- 主要适配 375px - 768px 屏幕

**适配规范**:
- iOS安全区域适配
- 底部导航适配安全区域
- 内容区域避免被系统UI遮挡

### 13. PC端与移动端共享

**可共享的部分**:
- ✅ Actions（业务逻辑）
- ✅ DTOs（数据契约）
- ✅ Models（数据模型）
- ✅ Enums（枚举类型）
- ✅ Events（领域事件）
- ✅ Jobs（异步任务）

**必须分离的部分**:
- ❌ Controllers（控制器）
- ❌ Routes（路由）
- ❌ Pages（页面组件）
- ❌ Layouts（布局组件）

**规则**:
- ✅ 业务逻辑层（Actions）可以共享
- ✅ 数据层（Models, DTOs）可以共享
- ❌ 表现层（Controllers, Pages）必须分离
- ❌ 路由必须分离，便于权限控制

### 14. PC端与移动端权限控制

**路由中间件**:
```php
// PC端路由组
Route::prefix('pc')->name('pc.')->middleware('auth')->group(function () {
    // PC端认证路由
});

// 移动端路由组
Route::prefix('m')->name('mobile.')->middleware('auth')->group(function () {
    // 移动端认证路由
});
```

**权限区分**:
- PC端和移动端可以使用不同的权限策略
- 通过路由前缀区分不同端的权限
- 便于未来实现不同端的菜单和权限控制
- PC端和移动端可以有不同的功能菜单

---

## 规范分类总结

### 统一规范（PC端和移动端共同遵循）

1. **后端开发规范**:
   - Action 类规范
   - DTO 规范
   - Model 规范
   - Enum 规范
   - 数据库规范

2. **代码风格规范**:
   - PHP 代码风格（PSR-12）
   - JavaScript 代码风格
   - 注释规范

3. **测试规范**:
   - 测试框架（Pest PHP）
   - 测试结构
   - 测试命名

4. **Git 提交规范**:
   - 提交消息格式
   - Type 类型

### PC端专用规范

1. **目录结构规范**:
   - PC端页面: `Pages/PC/` 目录
   - PC端布局: `Layouts/PC/` 目录
   - PC端控制器: `Controllers/PC/` 目录

2. **路由规范**:
   - `/pc` URL前缀
   - `pc.` 路由名称前缀

3. **前端组件规范**:
   - PC端布局（AppLayout）
   - PC端组件结构
   - PC端Inertia渲染路径: `PC/Module/Page`

4. **UI设计规范**:
   - PC端颜色、间距、字体
   - PC端交互规范
   - 2层菜单结构
   - 多Tab标签页管理

### 移动端专用规范

1. **移动端目录结构**:
   - `Pages/Mobile/` 目录
   - `Layouts/Mobile/` 目录
   - `Controllers/Mobile/` 目录

2. **移动端路由规范**:
   - `/m` 前缀
   - `mobile.` 路由名称前缀

3. **移动端布局规范**:
   - 底部导航栏
   - 响应式设计
   - iOS安全区域适配

4. **移动端UI设计规范**:
   - 移动端颜色、间距、字体
   - 移动端交互规范
   - 触摸友好设计

---

**最后更新**: 2025-12-18
**维护者**: Better10 开发团队

