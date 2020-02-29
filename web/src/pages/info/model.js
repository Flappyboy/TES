import { queryAllInfoTypes, addRule, queryInfosByType, removeRule, updateRule } from './service';

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
    },
    currentInfoType: null,
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
          list: response.result,
          pagination: {
            pageSize: 5,
          },
        },
      });
    },

    *chooseInfoType({ payload }, { put, select }){
      yield put({
        type: 'saveCurrentInfoType',
        payload: payload,
      });
      payload.projectId = yield select(
        state => state.project.currentProject.id
      );
      yield put({
        type: 'fetchInfosByType',
        payload: payload,
      });
    },

    *fetchInfosByType({ payload }, { call, put }){
      const response = yield call(queryInfosByType, {
        infoClass: payload.className,
        infoName: payload.infoName,
        pageSize: 10,
        pageNum: 1,
      });
      yield put({
        type: 'saveInfos',
        payload: {
          list: response.result,
          pagination: {
            pageSize: 10,
            pageNum: 1,
            total: response.total,
          }
        }
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

    saveInfos(state, action){
      return { ...state, data: action.payload };
    },

    saveInfoTypes(state, action) {
      return { ...state, infoTypes: action.payload };
    },

    saveCurrentInfoType(state, action){
      return { ...state, currentInfoType: action.payload};
    },
  },
};
export default Model;
