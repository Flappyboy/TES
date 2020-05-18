import { parse } from 'url';
// mock tableListDataSource
let tableListDataSource = [];

for (let i = 0; i < 1; i += 1) {
  tableListDataSource.push({
    key: 1,
    disabled: i % 6 === 6,
    href: 'https://ant.design',
    avatar: [
      'https://gw.alipayobjects.com/zos/rmsportal/eeHMaZBwmTvLdIwMfBpg.png',
      'https://gw.alipayobjects.com/zos/rmsportal/udxAbMEhpwthVVcjLXik.png',
    ][i % 2],
    name: `TCP`,
    title: `TCP`,
    owner: 'me',
    desc: '用例排序项目',
    callNo: Math.floor(Math.random() * 1000),
    status: 0,
    updatedAt: new Date(`2020-04-${Math.floor(i / 2) + 1}`),
    createdAt: new Date(`2020-04-${Math.floor(i / 2) + 1}`),
    progress: Math.ceil(Math.random() * 100),
  });
  tableListDataSource.push({
    key: 2,
    disabled: i % 6 === 6,
    href: 'https://ant.design',
    avatar: [
      'https://gw.alipayobjects.com/zos/rmsportal/eeHMaZBwmTvLdIwMfBpg.png',
      'https://gw.alipayobjects.com/zos/rmsportal/udxAbMEhpwthVVcjLXik.png',
    ][i % 2],
    name: `TES`,
    title: `TES`,
    owner: 'me',
    desc: '任务平台项目',
    callNo: Math.floor(Math.random() * 1000),
    status: 0,
    updatedAt: new Date(`2020-04-${Math.floor(i / 2) + 3}`),
    createdAt: new Date(`2020-04-${Math.floor(i / 2) + 3}`),
    progress: Math.ceil(Math.random() * 100),
  });
  tableListDataSource.push({
    key: 3,
    disabled: i % 6 === 6,
    href: 'https://ant.design',
    avatar: [
      'https://gw.alipayobjects.com/zos/rmsportal/eeHMaZBwmTvLdIwMfBpg.png',
      'https://gw.alipayobjects.com/zos/rmsportal/udxAbMEhpwthVVcjLXik.png',
    ][i % 2],
    name: `SSP`,
    title: `SSP`,
    owner: 'me',
    desc: '袜子商店项目',
    callNo: Math.floor(Math.random() * 1000),
    status: 0,
    updatedAt: new Date(`2020-04-${Math.floor(i / 2) + 3}`),
    createdAt: new Date(`2020-04-${Math.floor(i / 2) + 3}`),
    progress: Math.ceil(Math.random() * 100),
  });
}

function getRule(req, res, u) {
  let url = u;

  if (!url || Object.prototype.toString.call(url) !== '[object String]') {
    // eslint-disable-next-line prefer-destructuring
    url = req.url;
  }

  const params = parse(url, true).query;
  let dataSource = tableListDataSource;

  if (params.sorter) {
    const s = params.sorter.split('_');
    dataSource = dataSource.sort((prev, next) => {
      if (s[1] === 'descend') {
        return next[s[0]] - prev[s[0]];
      }

      return prev[s[0]] - next[s[0]];
    });
  }

  if (params.status) {
    const status = params.status.split(',');
    let filterDataSource = [];
    status.forEach(s => {
      filterDataSource = filterDataSource.concat(
        dataSource.filter(item => {
          if (parseInt(`${item.status}`, 10) === parseInt(s.split('')[0], 10)) {
            return true;
          }

          return false;
        }),
      );
    });
    dataSource = filterDataSource;
  }

  if (params.name) {
    dataSource = dataSource.filter(data => data.name.indexOf(params.name) > -1);
  }

  let pageSize = 10;

  if (params.pageSize) {
    pageSize = parseInt(`${params.pageSize}`, 0);
  }

  const result = {
    list: dataSource,
    pagination: {
      total: dataSource.length,
      pageSize,
      current: parseInt(`${params.currentPage}`, 10) || 1,
    },
  };
  return res.json(result);
}

function postRule(req, res, u, b) {
  let url = u;

  if (!url || Object.prototype.toString.call(url) !== '[object String]') {
    // eslint-disable-next-line prefer-destructuring
    url = req.url;
  }

  const body = (b && b.body) || req.body;
  const { method, name, desc, key } = body;

  switch (method) {
    /* eslint no-case-declarations:0 */
    case 'delete':
      tableListDataSource = tableListDataSource.filter(item => key.indexOf(item.key) === -1);
      break;

    case 'post':
      const i = Math.ceil(Math.random() * 10000);
      tableListDataSource.unshift({
        key: i,
        href: 'https://ant.design',
        avatar: [
          'https://gw.alipayobjects.com/zos/rmsportal/eeHMaZBwmTvLdIwMfBpg.png',
          'https://gw.alipayobjects.com/zos/rmsportal/udxAbMEhpwthVVcjLXik.png',
        ][i % 2],
        name: `TradeCode ${i}`,
        title: `一个任务名称23 ${i}`,
        owner: '曲丽丽',
        desc,
        callNo: Math.floor(Math.random() * 1000),
        status: Math.floor(Math.random() * 10) % 2,
        updatedAt: new Date(),
        createdAt: new Date(),
        progress: Math.ceil(Math.random() * 100),
      });
      break;

    case 'update':
      tableListDataSource = tableListDataSource.map(item => {
        if (item.key === key) {
          return { ...item, desc, name };
        }

        return item;
      });
      break;

    default:
      break;
  }

  const result = {
    list: tableListDataSource,
    pagination: {
      total: tableListDataSource.length,
    },
  };
  return res.json(result);
}

function getMetricsInfo(req, res) {
  const params = req.query;
  const count = params.count * 1 || 20;
  const result = sayHello(count);
  return res.json(result);
}

export default {
  'GET /api/rule': getRule,
  'POST /api/rule': postRule,
  'GET http://localhost:8080/yangdeyu/MetricsInfo/getInfo': getMetricsInfo,
};
