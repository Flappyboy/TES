import {queryArcSmell, queryFakeList, queryProject} from './service';
import {allArcSmellInfos} from "@/pages/arcsmell/service";

const Model = {
  namespace: 'arcsmell',
  state: {
    list: [],
    infos:[],
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
    *fetchInfos({ payload }, { call,put }){
      console.log('fetchInfos')
      const response = yield call(allArcSmellInfos, payload);
      yield put({
        type: 'saveAllArcsmellInfos',
        payload: {
          list: response.result,
          pagination: {
            pageSize: 5,
          },
        },
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
    saveAllArcsmellInfos(state, action) {
      return { ...state, infos: action.payload };
    },
  },
};
export default Model;
