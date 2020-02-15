import { queryAllInfoTypes, addRule, queryRule, removeRule, updateRule } from './service';

const Model = {
  namespace: 'infos',
  state: {
    data: {
      list: [],
      pagination: {},
    },
    infoTypes: {
      list: [],
      pagination: {},
    }
  },
  effects: {
    *fetch({ payload }, { call, put }) {
      /*const response = yield call(queryRule, payload);
      yield put({
        type: 'save',
        payload: response,
      });*/
    },

    *fetchInfoTypes({ payload }, { call, put }) {
      const response = yield call(queryAllInfoTypes, payload);
      yield put({
        type: 'saveInfoTypes',
        payload: {
          list: response,
          pagination: {
            pageSize: 5,
          },
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
      return { ...state, data: action.payload };
    },

    saveInfoTypes(state, action) {
      return { ...state, infoTypes: action.payload };
    },
  },
};
export default Model;
