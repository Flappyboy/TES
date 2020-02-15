import { queryTask, addRule, queryRule, removeRule, updateRule } from './service';

const Model = {
  namespace: 'tasks',
  state: {
    data: {
      list: [],
      pagination: {
        current: 1,
        pageSize: 10,
      },
    },
  },
  effects: {
    *fetch({ payload }, { call, put, select }) {
      payload.projectId = yield select(
        state => state.project.currentProject.id
      );
      const response = yield call(queryTask, payload);
      yield put({
        type: 'save',
        payload: {
          list: response.result,
          pagination: {
            current: response.pageNum,
            pageSize: 10,
            total: response.total,
          }
        },
      });
    },

    *add({ payload, callback }, { call, put }) {
      const response = yield call(addRule, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback();
    },

    *remove({ payload, callback }, { call, put }) {
      const response = yield call(removeRule, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback();
    },

    *update({ payload, callback }, { call, put }) {
      const response = yield call(updateRule, payload);
      yield put({
        type: 'save',
        payload: response,
      });
      if (callback) callback();
    },
  },
  reducers: {
    save(state, action) {
      console.log(action.payload)
      return { ...state, data: action.payload };
    },
  },
};
export default Model;
