import { queryFakeList, queryProject, queryMaintain } from './service';

const Model = {
  namespace: 'search',
  state: {
    list: [],
    data: [],
  },
  effects: {
    *fetch({ payload, callback}, { call, put }) {
      const response = yield call(queryMaintain, payload);
      callback(response)
      yield put({
        type: 'saveData',
        payload: Array.isArray(response) ? response : [],
      });
    },
    *chooseProject({ payload }, { put }) {
      yield put({
        type: 'project/changeProject',
        payload: payload,
      });
    },
  },
  reducers: {
    saveData(state, action) {
      return { ...state, data: action.payload };
    },
  },
};
export default Model;
