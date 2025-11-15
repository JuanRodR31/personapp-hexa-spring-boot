// Create user with permissions in the admin database
db = db.getSiblingDB('admin');

db.createUser({
  user: 'persona_db',
  pwd: 'persona_db',
  roles: [
    { role: 'read', db: 'persona_db' },
    { role: 'readWrite', db: 'persona_db' },
    { role: 'dbAdmin', db: 'persona_db' }
  ],
  mechanisms: ['SCRAM-SHA-1', 'SCRAM-SHA-256']
});

print('MongoDB user created successfully');
