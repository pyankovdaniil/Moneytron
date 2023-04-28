![Authentication Logo](../img/Authentication.png)

# 🔒 Authentication Microservice

This microservice is responsible for registration, authentication and refreshing users tokens.

# 📥 Endpoints

Now this microservice has three endpoints:

1. /api/v1/auth/register - User registration. You should send your data in JSON-format like this:

```json
{
    "fullName": "YOUR FULL NAME",
    "email": "YOUR EMAIL",
    "password": "YOUR PASSWORD"
}
```

- User password will be encoded using BCrypt algorithm and then user data will be stored in MongoDB. As a result, you will get 2 tokens: JWT and Refresh Token in JSON-format:

```json
{
    "accessToken": "YOUR JWT TOKEN",
    "refreshToken": "YOUR REFRESH TOKEN"
}
```

- If the email is already taken and stored in database, you will receive a message about it and 400 status code.

---

2. /api/v1/auth/register - User authentication. You should send an email and password to your account, and if they are valid, you will receive JWT and Refresh Token in a JSON-format described previously. If your email and password are not valid, you wont receive any tokens, only 400 status code and message that your data are not correct.

---

3.  /api/v1/auth/refresh - Refresh your JWT. If your JWT is expired, you can refresh it with this endpoint. All you have to do, is to send your Refresh Token, and you will receive new JWT in a JSON-format:

```json
{
    "newJwt": "YOUR NEW JWT"
}
```

- All refresh tokens are stored in a Redis database, so validating and refreshing a token is a very fast process.

---