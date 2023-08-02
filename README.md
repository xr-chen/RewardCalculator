# Reward Calculator

The `Reward Calculator` provides REST endpoints for managing customer reward information based on their purchases.

## Endpoints

### Get Rewards for a Specific Customer

#### Request

```http
GET /api/v1/rewards/{name}?month={month}
```

- `name` (path parameter): The account name of the customer.
- `month` (query parameter, optional): The specific month for which rewards are calculated. If not provided, rewards for the entire history are calculated.

#### Response

Returns the reward information for the specified customer and, for each month.

```json
{
  "name": "customer1",
  "total": 110,
  "rewards": {
    "1": 170
  }
}
```
#### Get Rewards for All Customers
Request

```http request
GET /api/v1/rewards
```
Response
Returns a list of reward information for all customers.
```json
[
  {
    "name": "customer1",
    "total": 110,
    "rewards": {
      "1": 170
    }
  },
  {
    "name": "customer2",
    "total": 30,
    "rewards": {
      "1": 20
    }
  }
]

```

### Usage
To use the RewardController, you need to make HTTP requests to the specified endpoints using your preferred client (e.g., a web browser, curl, Postman, etc.). The responses will provide you with reward information based on customer purchases.

