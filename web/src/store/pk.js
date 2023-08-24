export default {
    state: {
        status: "matching",  // matching表示匹配界面，playing表示对战界面
        socket : null, // 保存此次websocket链接
        opponent_username : '', // 对手的用户名
        opponent_photo : '', // 对手的头像
        gamemap : null, // 匹配成功之后后端传过来的地图
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
        updateGameMap(state, gamemap) {
            state.gamemap = gamemap
        }
    },
    actions: {
        
    },
    modules: {
    }
}