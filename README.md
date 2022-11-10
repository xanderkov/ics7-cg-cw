# Чашка со стержнем


Как это запустить? 

Нужно установить жаву

```bash
sudo apt install default-java
```

Далее по этой ссылке надо выполнить установку javafx

https://openjfx.io/openjfx-docs/

Открываем файл -> там Project structer

Там либы и туда добавить либу jfx (именно папку либы)

После этого настроить в параметрах запуска

```bash
--module-path "ПУТЬ ДО ЛИБЫ В ЖАВАФХ" --add-modules javafx.controls,javafx.fxml
```

Чтобы не забыть, есть эта ссылка: https://www.youtube.com/watch?v=Ope4icw6bVk

## Как это выглядит

![Пульт управления](./docs/rpz/assets/interface-2.png)
![Программа](./docs/rpz/assets/interface-1.png)
