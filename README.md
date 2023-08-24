homework-0: 
https://miro.com/app/board/uXjVMxMZ5NE=/

homework-1:
https://miro.com/app/board/uXjVMxMZ5NE=/

homework-2:
1. employee-auth -- сервис авторизации, регистрации. Поднимается локально
Рассматривал другой вариант -- использовать keycloak. авторизация/аутентификация из коробки, 
дополнительно нужно было бы
реализовать провайдер-плагин для вызова метода регистрации пользователя в account-сервисе, либо
можно было бы вообще без account-сервиса -- реализовать отправку данных о зарегистрированном пользователе,
смене роли и т.д в cтрим-топик напрямую в кафку из провайдера-плагина в keycloak. Хороший вариант, 
когда логика простая(просто застримить). Отдельный сервис даёт больше гибкости в добавлении новой логики,
но на реальном проекте подумал бы в сторону кейклока и провайдеров-плагинов.
2. api-gateway -- нужен для преобразования авторизации из запроса в модель user в сервисах 
task-tracker и analitics(accounting). Можно разделить GET и POST/PATCH маппинги и реализовать cqrs,
выделив отдельный сервис для read-моделей.
3. task-tracker -- сервис трекера задач. Цена считается в нём. Можно выделить сервис-адаптаптер, но пока так.
4. Скрипт с запуском postgres в postgresql. Для запуска приложений нужно руками создать 2 базы(в скрипте)
5. Скрипты с запросами для удобства в scripts в корне проекта. 
После Bearer нужно вставить токен, полученный
в результате выполнения скрипта token
6. В облачной инфре апи сервисов были бы закрыты, для простоты локального запуска закрывать не стал,
так что просто все запросы отправлять на 8080(api-gateway), но при желании можно напрямую в сервис.
Ограничение -- нужно сформировать хэдеры в запрос, по которым собирается объект user.
7. shuffle сделал следующим образом:
Создание shuffle через апи. Создавать можно несколько. Исполняет их джоба. Исполнение последовательное.
Выполнить задачу можно только если время её последнего обновления больше, чем время создания последнего 
shuffle. Джоба получает задачи бачами по 500 штук, поведение меняется параметром limit.
8. Для всех сервисов есть Dockerfile, но для простототы сервисы запускаются локально.
9. headers-resolver -- библиотека и spring-boot-starter для преобразования нескольких http хэдеров
в определённом формате в объект user.
10. kafka по ссылке из курса в докере -- kafka/cp-all-in-one-community


homework3:
1. в качестве стратегии обработки ошибок(что-то не долетело в stream топик, либо другая ошибка) 
выбрана стратегия N ретраев, затем отправка в dlq. dql пока в виде TODO
В дз реализована стратегия с N ретраями.
2. schema-registry был добавлен только для employee-stream топика -- в целях обучения.
Формат с библиотекой проще, типизация есть, но есть минуты:
 2.1. только jvm-based язык
 2.2 нужно следить за версией библиотеки в приложениях -- чтобы она везде совпадала
