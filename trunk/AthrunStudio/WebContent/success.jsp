<%-- 
    Document   : success
    Created on : Feb 28, 2009, 8:24:14 AM
    Author     : eswar@vaannila.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>

<s:iterator value="getDevices()" id="it" status="st">
	<li><strong class="content1"></strong>
		<div class="left">
			<table>
				<tbody>
					<tr>
						<td>厂商</td>
						<td><s:property value="manufacturer" /></td>
					</tr>
					<tr>
						<td>型号</td>
						<td><s:property value="model" /></td>
					</tr>
					<tr>
						<td>CodeName</td>
						<td><s:property value="device" /></td>
					</tr>
					<tr>
						<td>IP地址</td>
						<td><s:property value="ipAddress" /></td>
					</tr>
					<tr>
						<td>CPU指令集</td>
						<td><s:property value="cpuAbi" /></td>
					</tr>
					<tr>
						<td>SDK版本</td>
						<td><s:property value="sdk" /></td>
					</tr>
					<tr>
						<td>开始远程</td>
						<td><a
							href="remote.jsp?serialNumber=<s:property value='serialNumber' />"><s:property
									value="serialNumber" /></a></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="right" style="height: 200px;">
			<a href="remote.jsp?serialNumber=<s:property value='serialNumber' />"
				style="background:transparent url(/AthrunStudio/JpgGen.jpg?ts=0&serialNumber=<s:property value='serialNumber' />); width:120px; height:200px; background-size:cover; display:block;"
				title="点击图片开始远程"> 
			</a>
		</div></li>
</s:iterator>