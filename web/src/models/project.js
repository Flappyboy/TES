import request from '@/utils/request';


async function queryProject(params) {
  return request('/api/project',{
    pageNum: 1,
    pageSize: 1,
  });
}
const ProjectModel = {
  namespace: 'project',
  state: {
    currentProjectde: {
      "id": 1,
      "createdTime": 1575216488269,
      "updatedTime": 1575216488269,
      "name": "CT",
      "desc": "just test"
    },
    currentProject: {"id":null},
  },
  effects: {
    *chooseProject(payload, { call, put }) {
      // const response = yield call(queryCurrent);
      yield put({
        type: 'chooseProject',
        payload: payload,
      });
    },
    *init(payload, {call, put}){
      const response = yield call(queryProject);
      console.log("init");
      console.log(response);
      yield put({
        type: 'changeProject',
        payload: response.result[0],
      });
    }
  },
  reducers: {
    changeProject(state, action) {
      console.log("changeProject", action);
      const project = action.payload;
      if (project.id && state.currentProject.id!==project.id){
        console.log("choose"+project.id);
        return { ...state, currentProject: project || {} };
      }
      return { ...state }
    },
  },
};
export default ProjectModel;
