# Docker 开发环境

## 📁 项目结构

```
docker/
├── docker-compose.yml    # Docker Compose 配置文件
├── data/                 # 数据持久化目录
│   └── db/               # PostgreSQL 数据目录
```

## 🚀 快速启动

### 1. 启动所有服务

```bash
# 在 docker/ 目录下执行
cd docker
docker-compose up -d
```

### 2. 查看服务状态

```bash
docker-compose ps
```

### 3. 查看日志

```bash
# 查看所有服务日志
docker-compose logs -f

# 查看特定服务日志
docker-compose logs -f bt10-db     # 数据库
```

### 4. 停止服务

```bash
docker-compose down
```

### 5. 停止并删除数据（谨慎使用）

```bash
docker-compose down -v
```

### 进入数据库

```bash
docker exec -it bt10-db psql -U postgres -d bt10
```

## ⚙️ 配置说明

### 端口映射

- **5432**: PostgreSQL 数据库

### 目录映射

- `./data/db` → `/var/lib/postgresql/data` (数据库数据)

## 🗄️ 数据库配置

默认数据库配置：
- **数据库名**: bt10
- **用户名**: postgres
- **密码**: Bt10@1234
- **主机**: db (容器内) / localhost:5432 (宿主机)

4. **数据库初始化**：
   - 后端 tao-end 启动时会通过 Liquibase 自动执行 `db/changelog` 下的脚本，完成建表与初始数据；无需单独执行迁移命令。

## 🐛 故障排查

### 端口被占用

```bash
# 检查端口占用
lsof -i :8080  # macOS/Linux
netstat -ano | findstr :8080  # Windows

# 修改 docker-compose.yml 中的端口映射
```

### 数据库连接失败

```bash
# 检查数据库容器状态
docker-compose ps bt10-db

# 查看数据库日志
docker-compose logs bt10-db

# 等待数据库完全启动（约 30 秒）
sleep 30
```
