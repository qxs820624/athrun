

function findElement(root, text, index, elementType){
	__element = "UIAElementNil";
	__index = 0;
	target.pushTimeout(0);
	__findElement(eval(root),root, text, index, elementType);
	target.popTimeout();
	
	return __element;
}

function __findElement(root, script, text, index, elementType){
		
	var elements = root.elements();
	
	for(var i = 0 ; i<elements.length ;i++){
	
		if(__assertContainText(elements[i],text)){
			
			var obj = {};
			obj.guid = script + ".elements()[" + i + "]";
			obj.type = __getClass(elements[i]);
			obj.name =  elements[i].name();
			obj.value = elements[i].value();
			obj.label = elements[i].label();
			obj.rect = elements[i].rect();
			
			if(elementType == "UIAElement"){	
				if(__index == index){
					__element = JSON.stringify(obj);
					break;
				}
				__index++;
			}else if( __getClass(elements[i])== elementType){
				if(__index == index){
					__element = JSON.stringify(obj);
					break;
				}
				__index++;
			}
		}
		
		__findElement(elements[i] , script + ".elements()[" + i + "]", text,index , elementType);
		
	}
}

function findElements(root, text, elementType){
	__elementArray =[];
	target.pushTimeout(0);
	__findElements(eval(root),root, text, elementType);
	target.popTimeout();
	
	return JSON.stringify(__elementArray);
}

function __findElements(root, script, text, elementType){
		
	var elements = root.elements();
	
	for(var i = 0 ; i<elements.length ;i++){
	
		if(__assertContainText(elements[i],text)){
			
			var obj = {};
			obj.guid = script + ".elements()[" + i + "]";
			obj.type = __getClass(elements[i]);
			obj.name =  elements[i].name();
			obj.value = elements[i].value();
			obj.label = elements[i].label();
			obj.rect = elements[i].rect();
			
			if(elementType == "UIAElement"){	
				__elementArray.push(obj);
				UIALogger.logMessage("UIAElement : " + JSON.stringify(obj));	
			}else if( __getClass(elements[i])== elementType){
				__elementArray.push(obj);
			}
		}
		__findElements(elements[i] , script + ".elements()[" + i + "]", text , elementType);
		
	}
}

function printElementTree(root){
	
	__elementTree = "";
	target.pushTimeout(0);
	__logElementTree(eval(root),"", root);
	target.popTimeout();
	
	return __elementTree;
}

function __logElementTree(root,space,script){	
	
	var elements = root.elements();
	
	for(var i = 0 ; i<elements.length ;i++){
		
		var obj = {};
		obj.guid = script + ".elements()[" + i + "]";
		obj.type = __getClass(elements[i]);
		obj.name =  elements[i].name();
		obj.value = elements[i].value();
		obj.label = elements[i].label();
		obj.rect = elements[i].rect();
		__elementTree += "+" +space + JSON.stringify(obj) + "###";
		
		__logElementTree(elements[i] , space + "----", script + ".elements()[" + i + "]");
	}
}

function __getClass(object){
	
	return Object.prototype.toString.call(object).match(/^\[object\s(.*)\]$/)[1];	
}

function __assertContainText(obj, expectContain) {
	
	var name = obj.name();
	var labelText = obj.label();
	var value = obj.value();
	
	var isTrue1 = name == null ? false
				: (name.search(expectContain) != -1 ? true : false);
	var isTrue2 = labelText == null ? false : (labelText
				.search(expectContain) != -1 ? true : false);
	var isTrue3 = value == null ? false : (value.toString().search(expectContain) != -1 ? true : false);
	
	if (isTrue1 || isTrue2 || isTrue3) {
		return true;
	} else {
		return false;
	}
}

function __assertEqualsText(obj,text) {
	
	var name = obj.name();
	var labelText = obj.label();
	var value = obj.value();
	var isTrue1 = name == text ? true : false;
	var isTrue2 = labelText == text ? true : false;
	var isTrue3 = value == text ? true : false;
	
	if (isTrue1 || isTrue2 || isTrue3) {
		return true;
	} else {
		return false;
	}
}