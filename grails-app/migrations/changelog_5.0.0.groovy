databaseChangeLog = {

  changeSet(author: "metric", id: "5.0.0-1", context: "create, upgrade, 5.0.0") {
    comment("Creating metric table")
    createTable(tableName: "metric") {
      column(autoIncrement: "true", name: "id", type: "bigint") {
        constraints(nullable: "false", primaryKey: "true", primaryKeyName: "metricPK")
      }

      column(name: "version", type: "bigint") {
        constraints(nullable: "false")
      }

      column(name: "component", type: "varchar(200)") {
        constraints(nullable: "false")
      }

      column(name: "component_id", type: "varchar(255)") {
        constraints(nullable: "false")
      }

      column(name: "instance_id", type: "varchar(255)") {
        constraints(nullable: "false")
      }

      column(name: "metric_time", type: "varchar(255)") {
        constraints(nullable: "false")
      }

      column(name: "metric_type_id", type: "varchar(255)") {
        constraints(nullable: "false")
      }

      column(name: "site", type: "varchar(255)") {
        constraints(nullable: "false")
      }

      column(name: "user_agent", type: "varchar(255)") {
        constraints(nullable: "false")
      }

      column(name: "user_id", type: "varchar(255)") {
        constraints(nullable: "false")
      }

      column(name: "user_name", type: "varchar(255)") {
        constraints(nullable: "false")
      }

      column(name: "widget_data", type: "varchar(255)")
    }
  }

  changeSet(author: "metric", id: "5.0.0-2", context: "create, upgrade, 5.0.0") {
    createTable(tableName: "owf_group") {
      column(autoIncrement: "true", name: "id", type: "bigint") {
        constraints(nullable: "false", primaryKey: "true", primaryKeyName: "owf_groupPK")
      }

      column(name: "version", type: "bigint") {
        constraints(nullable: "false")
      }

      column(name: "automatic", type: "boolean") {
        constraints(nullable: "false")
      }

      column(name: "description", type: "varchar(255)")

      column(name: "email", type: "varchar(255)")

      column(name: "name", type: "varchar(200)") {
        constraints(nullable: "false")
      }

      column(name: "status", type: "varchar(8)") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "metric", id: "5.0.0-3", context: "create, upgrade, 5.0.0") {
    createTable(tableName: "owf_group_people") {
      column(name: "group_id", type: "bigint") {
        constraints(nullable: "false")
      }

      column(name: "person_id", type: "bigint") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "metric", id: "5.0.0-4", context: "create, upgrade, 5.0.0") {
    createTable(tableName: "person") {
      column(autoIncrement: "true", name: "id", type: "bigint") {
        constraints(nullable: "false", primaryKey: "true", primaryKeyName: "personPK")
      }

      column(name: "version", type: "bigint") {
        constraints(nullable: "false")
      }

      column(name: "description", type: "varchar(255)")

      column(name: "email", type: "varchar(255)")

      column(name: "email_show", type: "boolean") {
        constraints(nullable: "false")
      }

      column(name: "enabled", type: "boolean") {
        constraints(nullable: "false")
      }

      column(name: "last_login", type: "timestamp")

      column(name: "prev_login", type: "timestamp")

      column(name: "user_real_name", type: "varchar(200)") {
        constraints(nullable: "false")
      }

      column(name: "username", type: "varchar(200)") {
        constraints(nullable: "false", unique: "true")
      }
    }
  }

  changeSet(author: "metric", id: "5.0.0-5", context: "create, upgrade, 5.0.0") {
    createTable(tableName: "role") {
      column(autoIncrement: "true", name: "id", type: "bigint") {
        constraints(nullable: "false", primaryKey: "true", primaryKeyName: "rolePK")
      }

      column(name: "version", type: "bigint") {
        constraints(nullable: "false")
      }

      column(name: "authority", type: "varchar(255)") {
        constraints(nullable: "false", unique: "true")
      }

      column(name: "description", type: "varchar(255)") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "metric", id: "5.0.0-6", context: "create, upgrade, 5.0.0") {
    createTable(tableName: "role_people") {
      column(name: "role_id", type: "bigint") {
        constraints(nullable: "false")
      }

      column(name: "person_id", type: "bigint") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "metric", id: "5.0.0-7", context: "create, upgrade, 5.0.0") {
      addPrimaryKey(columnNames: "group_id, person_id", tableName: "owf_group_people")
  }

  changeSet(author: "metric", id: "5.0.0-8", context: "create, upgrade, 5.0.0") {
      addPrimaryKey(columnNames: "role_id, person_id", tableName: "role_people")
  }

  changeSet(author: "metric", id: "5.0.0-9", dbms:"hsqldb,mysql,mssql,postgresql", context: "create, upgrade, 5.0.0") {
      createIndex(indexName: "username_unique_1334248717317", tableName: "person", unique: "true") {
          column(name: "username")
      }
  }

  changeSet(author: "metric", id: "5.0.0-10", dbms:"hsqldb,mysql,mssql,postgresql", context: "create, upgrade, 5.0.0") {
      createIndex(indexName: "authority_unique_1334248717321", tableName: "role", unique: "true") {
          column(name: "authority")
      }
  }

  changeSet(author: "metric", id: "5.0.0-11", context: "create, upgrade, 5.0.0") {
      addForeignKeyConstraint(baseColumnNames: "group_id", baseTableName: "owf_group_people", constraintName: "FK28113703B197B21", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "owf_group", referencesUniqueColumn: "false")
  }

  changeSet(author: "metric", id: "5.0.0-12", context: "create, upgrade, 5.0.0") {
      addForeignKeyConstraint(baseColumnNames: "person_id", baseTableName: "owf_group_people", constraintName: "FK2811370C1F5E0B3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "person", referencesUniqueColumn: "false")
  }

  changeSet(author: "metric", id: "5.0.0-13", context: "create, upgrade, 5.0.0") {
      addForeignKeyConstraint(baseColumnNames: "person_id", baseTableName: "role_people", constraintName: "FK28B75E78C1F5E0B3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "person", referencesUniqueColumn: "false")
  }

  changeSet(author: "metric", id: "5.0.0-14", context: "create, upgrade, 5.0.0") {
      addForeignKeyConstraint(baseColumnNames: "role_id", baseTableName: "role_people", constraintName: "FK28B75E7870B353", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "role", referencesUniqueColumn: "false")
  }

  changeSet(author: "metric", id: "5.0.0-15", dbms: "oracle,postgresql", context: "create, upgrade, 5.0.0") {
      createSequence(sequenceName: "hibernate_sequence")
  }
}
