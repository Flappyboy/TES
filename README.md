```flow
user_input=>inputoutput: ActionName, Infos
action=>operation: Action

user_input->action
```

- info
  - infoName
  - infoType

| origin | target | n-m |
| - | - | - |
| info name | info type | n-1 |
| info | info repository | 1-1 |
