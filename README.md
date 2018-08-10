# DrawerLayout



点击右侧抽屉划出，支持侧滑回弹，ViewDragHelper

**注意**：`ViewDragHelper`是作用在一个`ViewGroup`上，也就是说他不能直接作用到被拖拽的控件`view`上， 因为控件的位置是由父控件决定的。
初始状态
![image](https://github.com/chpchen/DrawerLayout/blob/master/app/src/main/res/mipmap-xhdpi/close.png)

展开时

![image](https://github.com/chpchen/DrawerLayout/blob/master/app/src/main/res/mipmap-xhdpi/open.png)

并且可以手势关闭，关闭时回调事件

参考
http://blog.csdn.net/u012551350/article/details/51601985

