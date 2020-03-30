import {queryArcSmell, queryFakeList, queryProject} from './service';

const Model = {
  namespace: 'arcsmell',
  state: {
    list: [],
  },
  effects: {
    *fetchList({ payload }, { call, put }) {
      const response = yield call(queryArcSmell, payload);
      /*console.log("response:");
      console.log(response);*/
      const list = response.result;
      yield put({
        type: 'queryList',
        payload: Array.isArray(list) ? list : [],
      });
    },
    *fetch({ payload }, { call, put }) {
      const response = yield call(queryFakeList, payload);
      yield put({
        type: 'queryList',
        payload: Array.isArray(response) ? response : [],
      });
    },
    *chooseProject({payload}, {put}){
      yield put({
        type: 'project/changeProject',
        payload: payload,
      });
    }
  },
  reducers: {
    queryList(state, action) {
      return { ...state, list: action.payload };
    },
  },
};
export default Model;
