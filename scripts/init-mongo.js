db = db.getSiblingDB('persona_db');

// Create user for the persona_db database
db.createUser({
  user: 'persona_db',
  pwd: 'persona_db',
  roles: [
    {
      role: 'readWrite',
      db: 'persona_db'
    }
  ]
});

print('MongoDB user created successfully');
