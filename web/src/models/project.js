const ProjectModel = {
  namespace: 'project',
  state: {
    currentProject: {},
  },
  effects: {
    *chooseProject(payload, { call, put }) {
      // const response = yield call(queryCurrent);
      yield put({
        type: 'chooseProject',
        payload: payload,
      });
    },
  },
  reducers: {
    changeProject(state, action) {
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
