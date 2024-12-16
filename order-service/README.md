**Order-Service**
Order service is used to create the order and generate the order receipt.

***Order-service is having below urls-***

1.) localhost:8080/api/order -> This url is used to create order and add order items in order.
   RequestBody:
{
"status": "NEW",
"items": [
{
"itemName": "Bread",
"itemType": "BREAD",
"manufacturedDate": "2024-12-13T00:00:00Z",
"quantity": 4,
"weight": 2.5,
"actualPrice": 4
},
{
"itemName": "Bread",
"itemType": "BREAD",
"manufacturedDate": "2024-12-15T00:00:00Z",
"quantity": 3,
"weight": 2.5,
"actualPrice": 3
},
{
"itemName": "Beer",
"itemType": "BEER",
"manufacturedDate": "2023-10-15T00:00:00Z",
"quantity": 8,
"weight": 0.2,
"actualPrice": 4
},
{
"itemName": "Vegetables",
"itemType": "VEGETABLES",
"manufacturedDate": "2023-10-15T00:00:00Z",
"quantity": 1,
"weight": 200,
"actualPrice": 4
}
]
}

2.) localhost:8080/api/order/generateReceipt -> This url is used to generate the receipt of order.
    It accept one parameter e.g: orderNum