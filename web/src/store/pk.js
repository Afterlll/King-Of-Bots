export default {
    state: {
        status: "matching",  // matching表示匹配界面，playing表示对战界面
        socket : null, // 保存此次websocket链接
        opponent_username : '', // 对手的用户名
        opponent_photo : '', // 对手的头像
        gamemap : null, // 匹配成功之后后端传过来的地图
        a_id: 0, // 玩家a的id
        a_sx: 0, // 玩家a起点横坐标
        a_sy: 0, // 玩家a起点纵坐标
        b_id: 0, // 玩家b的id
        b_sx: 0, // 玩家b起点横坐标
        b_sy: 0, // 玩家b起点纵坐标
        gameObject : null, // 当前游戏对象
        loser : 'none', // 失败者
        eye_direction_a : '', // 玩家a游戏结束时眼睛的方向
        eye_direction_b : '', // 玩家b游戏结束时眼睛的方向
    },
    getters: {
    },
    mutations: {
        updateSocket(state, socket) {
            state.socket = socket
        },
        updateOpponent(state, opponent) {
            state.opponent_username = opponent.username 
            state.opponent_photo = opponent.photo
        },
        updateStatus(state, status) {
            state.status = status
        },
        updateGame(state, game) {
            state.gamemap = game.map;
            state.a_id = game.a_id;
            state.a_sx = game.a_sx;
            state.a_sy = game.a_sy;
            state.b_id = game.b_id;
            state.b_sx = game.b_sx;
            state.b_sy = game.b_sy;
        },
        updateGameObject(state, gameObject) {
            state.gameObject = gameObject
        },
        updateLoser(state, loser) {
            state.loser = loser
        },
        updateEyeDirectionA(state, a) {
            state.eye_direction_a = a
        },
        updateEyeDirectionB(state, b) {
            state.eye_direction_b = b
        }
    },
    actions: {
        
    },
    modules: {
    }
}