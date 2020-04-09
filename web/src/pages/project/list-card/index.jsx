import { Button, Card, Icon, List, Typography } from 'antd';
import React, { Component } from 'react';
import { PageHeaderWrapper } from '@ant-design/pro-layout';
import { connect } from 'dva';
import styles from './style.less';

const { Paragraph } = Typography;

@connect(({ projects, loading }) => ({
  projects,
  loading: loading.models.projects,
}))
class CardListForProject extends Component {
  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({
      type: 'projects/fetchList',
      payload: {
        pageNum: 0,
        pageSize: 10,
      },
    });
  }

  chooseProject(project) {
    const { dispatch } = this.props;
    console.log(this.props)
    dispatch({
      type: 'projects/chooseProject',
      payload: project,
    });
  }

  render() {
    const {
      projects: { list },
      loading,
    } = this.props;
    console.log(list);
    const content = (
      <div className={styles.pageHeaderContent}>
        <p>

        </p>
        <div className={styles.contentLink}>
          <a>
            <img alt="" src="https://gw.alipayobjects.com/zos/rmsportal/NbuDUAuBlIApFuDvWiND.svg" />{' '}
            工具简介
          </a>
          <a>
            <img alt="" src="https://gw.alipayobjects.com/zos/rmsportal/ohOEPSYdDTNnyMbGuyLb.svg" />{' '}
            工具文档
          </a>
        </div>
      </div>
    );
    const extraContent = (
      <div className={styles.extraImg}>
        <img
          alt="这是一个标题"
          src="https://gw.alipayobjects.com/zos/rmsportal/RzwpdLnhmvDJToTdfDPe.png"
        />
      </div>
    );
    const nullData = {};
    return (
      <PageHeaderWrapper
        //content={content} extraContent={extraContent}
      >
        <div className={styles.cardList}>
          <List
            rowKey="id"
            loading={loading}
            grid={{
              gutter: 24,
              lg: 3,
              md: 2,
              sm: 1,
              xs: 1,
            }}
            dataSource={[nullData, ...list]}
            renderItem={item => {
              if (item && item.id) {
                return (
                  <List.Item key={item.id}>
                    <Card
                      hoverable
                      className={styles.card}
                      actions={[<a key="option1" onClick={this.chooseProject.bind(this, item)}>选择</a>, <a key="option2">编辑</a>]}
                    >
                      <Card.Meta
                        avatar={<img alt="" className={styles.cardAvatar} src={item.avatar} />}
                        title={<a>{item.name}</a>}
                        description={
                          <Paragraph
                            className={styles.item}
                            ellipsis={{
                              rows: 3,
                            }}
                          >
                            {item.desc}
                          </Paragraph>
                        }
                      />
                    </Card>
                  </List.Item>
                );
              }

              return (
                <List.Item>
                  <Button type="dashed" className={styles.newButton}>
                    <Icon type="plus" /> 新增项目
                  </Button>
                </List.Item>
              );
            }}
          />
        </div>
      </PageHeaderWrapper>
    );
  }
}

export default CardListForProject;
