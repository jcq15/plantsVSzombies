# 简介

Java大作业：植物大战僵尸

需求见`need.pdf`

# 程序架构与命名规范

## 文件

- MainWindow：UI
- GameController：游戏逻辑
- Plant：植物
- Zombie：僵尸

UI层读取用户操作，调用GameController的接口进行处理，然后GameController返回新的画面信息交给UI层。也许不要gay controller了。

## 命名

MainWindow文件：

btn前缀：按钮

lsn前缀：事件监听器

# 可能的修改方向

- 僵尸出现改为泊松分布
- 增强鲁棒性，比如输入一亿个僵尸就骂用户一顿

# 一些东西

- 强制16：9，不是就按倒霉处理
- 不要按钮了 直接响应鼠标区域