�򵥽��ܣ�

1.InstrumentDriver ������instrument uiautomationʵ��C/S ģʽ�ķ�ʽ���нű���mobile����ΪClient��PC����ΪServer��������PC�˱�д����¼��javascript�ű���ʽ����������Junit��Ԫ���Է�ʽ���в���������


2.ԭ��instrumentuiautomation �ṩ���ڽű��е����ⲿshell�Ĺ��ܣ������ܻ�ȡ��shell���е������
�ݴˣ�������Mac������instrument����һ���ű���������ű�����shell��Զ�̵�PC����ͨ�Ż�ȡ���͹����Ľű���ִ�С�

  ��Ϊû����instrument javascript�ű��ｨ������ˣ�����ֻ����Mobile��Ϊ�ͻ��ˣ�����������Ϊsocket�ķ���ˣ��ͻ���ѭ����������(������������)����������ͣ�������С�ֱ�����ַ��ͽ����������׳��쳣��

����ļ���
  �ͻ�����Ҫ���������ļ���CSRunner.js(JSLibĿ¼)�ļ����� TcpSocket.sh �� �����Junit ������������ͨ�������С�TcpSocket.sh ��ΪC/S ˫����ͨ���н飬�� CSRunner.js �������������˵��������衣(TcpSocket.sh ��AthrunĿ¼��)

3.�ṩ����shell �ķ������£�host.performTaskWithPathArgumentsTimeout("/Athrun/Test.sh", ["null"], 3);��Test.sh ��ͨ������tcp������PC�˵�Serverͨ�ţ�������Ҫִ�еĲ��衣����ʱ����Ҫ��������Ԫ���ԣ�Ȼ������instrument uiautomation ���У���server�����������ȡ���ص���Ӧִ�С�

4.���⣺host.performTaskWithPathArgumentsTimeout() ���������ٶ��е�������Ϊ�᲻ͣ�ĵ������shell����server�����������Щƿ����




�Ľ���
	
    Ŀǰͨ�����ٷ������ͻ��˵�ͨ�Ŵ��������ű������ٶȡ��ű���debugģʽ�ͷ�debugģʽ���ַ�ʽ���У�debugģʽ�����ٶȽ��������Ե��Խű����鿴��ض���ı���ֵ����debugģʽ����Ҫ����UIԪ�ػ��ȡUIԪ�����Ե�ʱ���ͨ�ţ��ϴ�������������ٶ�(debugģʽ��2������)��
    ��������£�������ô��Ϊ��debugͨ���󣬷�debugģʽҲ������ͨ����
     
    �ṩ�˲���Ԫ�صķ���: findElemenetByText(String text)��findElementArrayByText(String text) �Լ�������ء�

