import { Form, Input, Modal } from 'antd';
import React from 'react';
import InfoSelector from "@/pages/arcsmell/components/InfoSelector";

const FormItem = Form.Item;

const InfoSelectorModal = props => {
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
      // destroyOnClose
      title="选择Info类型"
      visible={modalVisible}
      width={1000}
      onOk={okHandle}
      onCancel={() => handleModalVisible()}
    >
    <InfoSelector />
    </Modal>
  );
};

export default Form.create()(InfoSelectorModal);
