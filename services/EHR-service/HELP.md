# EHR Service

Electronic Health Records Service for Smart Healthcare System.

## Port
- Default: 8083

## Database
- Default: MySQL on port 3310
- Database name: ehrdb

## Endpoints

### Get EHR by Patient ID
```
GET /ehr/patient/{patientId}
```

### Get EHR by User ID
```
GET /ehr/user/{userId}
```

## Dependencies
- Patient Service (port 8081)
- Doctor Service (port 8082)


