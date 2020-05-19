import { Form, Input, Modal } from 'antd';
import React from 'react';

const FormItem = Form.Item;

const CreateForm = props => {
  const { modalVisible, form, handleAdd, handleModalVisible } = props;

  const okHandle = () => {
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      form.resetFields();
      handleAdd(fieldsValue);
    });
  };

  return (
    <Modal
      destroyOnClose
      title="新建项目"
      visible={modalVisible}
      onOk={okHandle}
      onCancel={() => handleModalVisible()}
    >
      <FormItem
        labelCol={{
          span: 5,
        }}
        wrapperCol={{
          span: 15,
        }}
        label="项目名"
      >
        {form.getFieldDecorator('name', {
          rules: [
            {
              required: true,
              message: '请输入项目名',
            },
          ],
        })(<Input placeholder="请输入项目名" />)}
      </FormItem>
      <FormItem
        labelCol={{
          span: 5,
        }}
        wrapperCol={{
          span: 15,
        }}
        label="描述"
      >
        {form.getFieldDecorator('desc', {
          rules: [
            {
              required: false,
            },
          ],
        })(<Input placeholder="请输入项目描述" />)}
      </FormItem>
    </Modal>
  );
};

export default Form.create()(CreateForm);
