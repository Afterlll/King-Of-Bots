## SpringBoot项目 --- King Of Bots


### 项目介绍
* 名称：King Of Bots，简称 KOB。
* 项目简介：
在线匹配对抗游戏是一款基于多人在线竞技的电子游戏。玩家通过互联网平台，可以快速匹配到实力相当的对手，进行实时对战。游戏拥有丰富的游戏模式，玩家可以选择真人对战、人机对战或与机机对战。在对战过程中，玩家需要运用策略、技巧和运筹帷幄，击败对手，赢得胜利，这里的机器人是通过代码驱动的，是智能机器人，并不是简单的人机，提升游戏的竞技性。
* 核心玩法：
1. 匹配机制：游戏采用先进的匹配算法，根据玩家的实力、胜率等因素，为玩家匹配到合适的对手。
2. 游戏模式：提供多种游戏模式，包括真人对战、人机对战或与机机对战，满足不同玩家的需求。
3. 英雄角色：游戏拥有丰富的英雄角色，每个英雄都有独特的技能和属性，玩家可以根据自己的喜好和战术选择合适的英雄。在这里就是机器人Bot。
4. 地图设计：游戏地图保证公平性，中心对称且初始位置合理，保证有效的正常进行。
5. 实时对战：玩家可以与来自全球的对手进行实时对战，体验紧张刺激的竞技乐趣。
* 商业模式：
1. 游戏内购买：提供各种虚拟商品和道具，玩家可以通过购买提升游戏体验。
2. 广告投放：在游戏界面展示广告，为广告主提供精准的广告投放平台。
3. 直播赞助：吸引品牌赞助商赞助直播活动，提升品牌知名度和曝光率。

### 代码位置
* https://app5765.acapp.acwing.com.cn/user/account/login/
* https://github.com/Afterlll/kob/


###主要技术

* 版本控制: java8、SpringBoot 2.3.7.RELESE、SpringCloudAlibaba 2021.0.3

* 前端技术：bootstrap、vue3、ajax、elementui

* 后端技术：SpringCloud、SpringBoot、MybatisPlus、WebSocke、SpringSecurity、joor

### 模块介绍
* PK模块：匹配界面（微服务）、实况直播界面（WebSocket协议）
* 对局列表模块：对局列表界面、对局录像界面
* 排行榜模块：Bot排行榜界面
* 用户中心模块：注册界面、登录界面、我的Bot界面、每个Bot的详情界面


### 功能描述
* 登录模块
使用jwt 登录方式实现用户身份认证、授权、单点登录、信息交换、跨域通信，解决传统session登录在微服务环境下的弊端，实现更加可靠的微服务环境下的登录。
<img width="431" alt="image" src="https://github.com/Afterlll/kob/assets/116958691/3ee96ef8-cb5a-478b-ae21-657583bac11d">


* 注册模块
给玩家提供注册功能，提升用户量。
<img width="431" alt="image" src="https://github.com/Afterlll/kob/assets/116958691/5aae9cd7-5bc5-4803-9132-f35bf6ab367d">

* 排行榜
根据玩家通过真人对战、人机对战、机机对战都可以提高自己的天梯分，这里是在全服进行排名，进行分页显示。
<img width="431" alt="image" src="https://github.com/Afterlll/kob/assets/116958691/08c8bef2-583e-4482-ba41-8fdf2b7660a4">

* 对局列表
显示两个玩家的对局结果，对战时间，此记录目前全服可查看，并且同时可以查看录像，可作为举报的材料。
<img width="431" alt="image" src="https://github.com/Afterlll/kob/assets/116958691/e1d94a54-640a-4c40-bbc9-bf3856020212">


* 查看录像
完整显示对局过程，并且会显示玩家位置，玩家对战结果。
<img width="431" alt="image" src="https://github.com/Afterlll/kob/assets/116958691/2a23192c-56e3-4ac5-b024-facc31efb889">

* 删除Bot
删除bot机器人
<img width="431" alt="image" src="https://github.com/Afterlll/kob/assets/116958691/55dcc9c9-abd5-4dac-9e5e-7e6d27249e98">


* 创建Bot
创建bot机器人，也就是写支撑机器人跑起来的代码，目前实现的沙箱环境支持java，并且代码编辑界面支持高亮、缩进、颜色优化等等，并且一个玩家最多创建10个Bot。
<img width="431" alt="image" src="https://github.com/Afterlll/kob/assets/116958691/a45dcc8d-9b5b-4da3-94d3-aac6c87ba92d">


* 修改Bot
修改Bot机器人的信息，包括标题，简介，关键代码等等。
<img width="431" alt="image" src="https://github.com/Afterlll/kob/assets/116958691/923dea3c-58b9-4821-9b1f-30163dcd285c">


* 对战匹配
玩家根据天梯分、胜率、匹配时间进行匹配，并且选择出战的方式 —— 亲自出马，选择Bot机器人出战。
<img width="431" alt="image" src="https://github.com/Afterlll/kob/assets/116958691/95463191-165e-4fef-ba71-54b593b6e25f">


* 匹配成功
玩家匹配成功之后，分配地图进行对战，哪一方先靠策略堵死对象就获胜，或者一方在五秒（可调整）内无操作，视为影响游戏正常进行，判负。
<img width="431" alt="image" src="https://github.com/Afterlll/kob/assets/116958691/692d7fdd-3126-4ee1-8434-65f79d129e82">
