# ShadowLayout
阴影布局
* 用path的布尔操作代替了drawBitmap。避免在draw方法里new对象
* 解决点击button阴影消失<br/>
***
## 效果图
* 阴影偏移
* 四周阴影
* 指定边的阴影
## 存在的问题
* 阴影偏移后会出现白点，图2（已解决，如图1）
* 布局包裹textview正常，当布局包裹的是button，点击button后阴影会消失(已解决)
<div align="center">
<img src="http://ww1.sinaimg.cn/large/006nwaiFly1g3i4vyxa1wj30cv0nqgmg.jpg" height = "400">
<img src="http://ww1.sinaimg.cn/large/006nwaiFly1g3hga908q7j30an0jcq38.jpg" height="400">

<img src="http://ww1.sinaimg.cn/large/006nwaiFly1g3hgdsaq2kj30aj0iymxf.jpg" height="400">

<img src="http://ww1.sinaimg.cn/large/006nwaiFly1g3hgecu8quj30ae0iq3yp.jpg" height="400">

 </div>
