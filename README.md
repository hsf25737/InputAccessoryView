# InputAccessoryView in Android

在Android上模仿ios的`InputAccessoryView`, 特点：
1. 随键盘一起出现，随键盘一起消失
2. 悬停在键盘上
3. 用Fragment实现，可在内部实现交互逻辑

使用简单：
```Java
new InputAccessoryPanelFragment()
                .withActivity(this)
                .replace(R.id.input_accessory_container)
                .attachEditText(editText);
```

模仿UC浏览器的输入增强栏效果:
<img src="./screenshot/1.gif" width = "329" height = "587" align=center />
