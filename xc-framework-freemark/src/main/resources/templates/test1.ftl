<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>hello world</title>
</head>
<body>
<h2>Hello ${name}</h2>
<hr>
<#list students as stu>
    <ul>
        <li>${stu_index}</li>
        <li>${stu.name}</li>
        <li>${stu.age}</li>
    </ul>
</#list>
<hr>
姓名：${studentMap['tongliya'].name}
年龄：${studentMap['tongliya'].age}
朋友们：<#list studentMap['tongliya'].friends as friend>
    <span>${friend.name}</span>
</#list>
<hr>
姓名：${studentMap.yangmi.name}
年龄：${studentMap.yangmi.age}
<hr>
<#list studentMap?keys as k>
    <ul>
    <#if studentMap[k].name=='佟丽娅'>
        <li style="background-color: azure">${k_index+1}</li>
        <li style="background-color: azure">${studentMap[k].name}</li>
        <li style="background-color: azure">${studentMap[k].age}</li>
    <#else>
        <li>${k_index+1}</li>
        <li>${studentMap[k].name}</li>
        <li>${studentMap[k].age}</li>
        <#if studentMap[k].money??
        <li >${studentMap[k].money!''}</li>
        </#if>
    </#if>
    </ul>
</#list>
</body>
</html>