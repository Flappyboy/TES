export default {
  // 支持值为 Object 和 Array
  'GET /api/project': {
    "result": [
      {
        "id": 1,
        "createdTime": 1575216488269,
        "updatedTime": 1575216488269,
        "name": "tes",
        "desc": "just test"
      },
      {
        "id": 2,
        "createdTime": 1575216428502,
        "updatedTime": 1575216428502,
        "name": "tes4",
        "desc": "just test"
      },
    ],
    "pageNum": 1,
    "pageSize": 10,
    "total": 2,
  },
  'POST /api/project': (req, res) => {
    const { password, userName, type } = req.body;

    if (password === 'ant.design' && userName === 'admin') {
      res.send({
        status: 'ok',
        type,
        currentAuthority: 'admin',
      });
      return;
    }

    if (password === 'ant.design' && userName === 'user') {
      res.send({
        status: 'ok',
        type,
        currentAuthority: 'user',
      });
      return;
    }

    res.send({
      status: 'error',
      type,
      currentAuthority: 'guest',
    });
  },
};
