export default {
    state: {
        is_record: false, // 是否是录像
        a_steps: "", // a玩家的运行轨迹
        b_steps: "", // b玩家的运行轨迹
        record_loser: "", // 当前观看录像的败者
    },
    getters: {
    },
    mutations: {
        updateIsRecord(state, is_record) {
            state.is_record = is_record;
        },
        updateSteps(state, data) {
            state.a_steps = data.a_steps;
            state.b_steps = data.b_steps;
        },
        updateRecordLoser(state, loser) {
            state.record_loser = loser;
        }
    },
    actions: {
    },
    modules: {
    }
}
