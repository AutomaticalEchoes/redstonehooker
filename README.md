# 偷盗工程师/红石小偷(RedstoneHooker)
[![Build Status](https://img.shields.io/badge/MinecraftForge-1.19.x-brightgreen)](https://github.com/MinecraftForge/MinecraftForge?branch=1.19.x)

_阅前提示：本人喜欢用<span title="如果影响你观看就先给你道个歉啦！>-<" >**`…`**</span>来添加注释。_
## 介绍

本模组添加了几个代理方块，能够**获取指定坐标方块的容器、红石信号强度** 和 **获取指定生物<span title="可指定的生物类型有限，有约束条件" >**`…`**</span>的背包容器**。  

###关键词
  
**`伪装`：** 方块常规状态下会伪装成其他方块，细节上有区别。   

**`看破`：** 能够看穿`伪装`。

**`代理方块`：** 代理方块在没有指定代理对象时与一般固体方块无异，无特殊效果。在指定代理对象后，方块会尝试获取代理对象，如果对象符合条件，代理方块会根据自身性质
代理对方的某项特性<span title="属性、能力等" >**`…`**</span>。此时，方块和目标特性是一致的，例如使用`实体容器代理箱`并指定代理对象为玩家，此时`实体容器代理箱`等效于`玩家背包`，
在其下方放置一个漏斗，玩家背包中的物品就会顺着漏斗流出。同理、在其上方接入漏斗，漏斗以及漏斗上方的物品会传输到玩家的背包中去。
只要设计一套过滤系统，就可以将那些有用但非常占用背包空间的物品直接通过漏斗流入工作系统或储藏系统，做到生产采集两不误。

#### 注意：代理方块的本质依旧是方块实体，只有在区块加载时才会工作，使用时请酌情选址或者配套区块加载器系统
#### 声明：开发版本，不代表模组最终玩法与效果<span title="本说明书也是&#10;多提提建议好让我改~~摆了！~~" >**`…`**</span>。

## 方块与物品
### 调节器（HookerAdjust）
**主手：**  
1.`看破`。  
2.显示代理方块的详细信息。  
3.对方块使用时，若副手手持`地址标签`，标签会记录方块地址<span title="也就是坐标" >**`…`**</span>。  
4.对方块使用时，若对象方块为`代理方块`且含有地址标签，会弹出标签使其成为凋落物。  

**其他：**  
在锻造台中，可以作为素材与头盔类型装备融合，为其添加`偷盗工程师`签名。玩家穿戴含该签名的头盔时，`看破`。

### 标签/地址标签（AddressTag）
**主手：**  
1.对方块使用时，若对象方块为`代理方块`且地址格式符合，消耗并置入。若该方块已含有地址标签，还会弹出旧标签使其成为凋落物。 
  
**其他：**  
含有地址记录时，名称优先显示记录信息。  
### 命名标签（AddressNameTag）
**主手：**  
1.对生物使用时，若标签已命名，将命名该生物，转换成`地址标签`并记录生物唯一标识<span title="生物的UUID" >**`…`**</span>。  
特别的，如果命名为`self`,玩家右键使用持续一段时间后，会记录玩家的唯一标识，要获取其他玩家的唯一标识，必须让其自己使用。

### 方块实体容器代理箱（ContainerProxyBlock）
符合以下条件时，启动：  
1.含有地址标签，且地址格式合法。  
2.坐标地址存在且地址的方块实体符合条件。  
启动时，代理指定坐标方块的容器。无GUI，不可查看代理容器详情。

### 实体容器代理箱（InventoryProxyBlock）
符合以下条件时，启动：  
1.含有地址标签，且地址格式合法。  
2.标识生物存在且生物符合条件。  
启动时，代理指定生物的容器。无GUI，不可查看代理容器详情。  

### 信号监听器 *未完成*（AnisotropicSignalBlock）
信号监听器各个面都可以置入`地址标签`。  
独立的，每个面输出的红石信号强度与监听地址的信号强度一致。  
*红石信号具有方向性，`地址标签`记录方块坐标地址时还会记录方向，但是由于未添加该方块，不显示。  
功能其实做好了，但是玩家使用时方向问题容易搞混<span title="参照系不同。以自己为参照和以相邻方块为参照，方向是相反的。" >**`…`**</span>，配套显示更新后再加入模组。*


--------------------------------------------------------
**非专业moder,望大家多多海涵.  
如果你发现了什么问题或者有什么建议,可以发邮件给我.~~回不回复随缘~~  
email:AutomaticalEchoes@outlook.com.**