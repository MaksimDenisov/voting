# Voting system for deciding where to have lunch.

### Quality by Code Climate
[![Maintainability](https://api.codeclimate.com/v1/badges/14ef72a81d1e28d04ee3/maintainability)](https://codeclimate.com/github/MaksimDenisov/voting/maintainability)

[![Test Coverage](https://api.codeclimate.com/v1/badges/14ef72a81d1e28d04ee3/test_coverage)](https://codeclimate.com/github/MaksimDenisov/voting/test_coverage)

### Task
Design and implement a REST API using Spring-Boot/Spring Data JPA without frontend.

The task is:

Build a voting system for deciding where to have lunch.

- 2 types of users: admin and regular users
- Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
- Menu changes each day (admins do the updates)
- Users can vote for a restaurant they want to have lunch at today
- Only one vote counted per user
- If user votes again the same day:
  - If it is before 11:00 we assume that he changed his mind.
  - If it is after 11:00 then it is too late, vote can't be changed
Each restaurant provides a new menu each day.

### Swagger
```
/api-docs
```

### H2 Console available by admin
```
/h2console
```

###  Checkstyle
```sh
make checkstyle
```
