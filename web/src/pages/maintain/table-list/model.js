import { addRule, queryDataset, addDataset, delDataset, removeRule, updateRule } from './service';

const Model = {
  namespace: 'listAndtableList',
  state: {
    data: {
      list: [],
      pagination: {},
    },
  },
  effects: {
    *fetch({ payload }, { call, put }) {
      const response = yield call(queryDataset, payload);
      // yield put({
      //   type: 'save',
      //   payload: response,
      // });
      yield put({
        type: 'save',
        payload: {
          list: response,
          pagination: {
            pageSize: 5,
          },
        },
      });
    },

    *add({ payload, callback }, { call, put }) {
      const response = yield call(addDataset, payload);
      // yield put({
      //   type: 'save',
      //   payload: response,
      // });
      if (callback) callback();
    },

    *remove({ payload, callback }, { call, put }) {
      console.log(payload)
      const response = yield call(delDataset, payload);
      // yield put({
      //   type: 'save',
      //   payload: response,
      // });
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
      return { ...state, data: action.payload };
    },
  },
};
export default Model;
