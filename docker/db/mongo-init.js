db = db.getSiblingDB('raspay'); // nome do banco

db.createCollection('products');

db.products.insertMany([
  {
    acronym: "MONTH22",
    current_price: 75.00,
    dt_creation: new Date(),
    name: "PLANO MENSAL"
  },
  {
    acronym: "YEAR22",
    current_price: 697.00,
    dt_creation: new Date(),
    name: "PLANO ANUAL"
  },
  {
    acronym: "PERPETUAL22",
    current_price: 1497.00,
    dt_creation: new Date(),
    name: "PLANO VITALÍCIO"
  }
]);
