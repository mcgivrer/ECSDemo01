# Spécification de la classe `App`

## Description générale

La classe `App` constitue la base d'une application orientée services. Elle gère le cycle de vie de l'application en
orchestrant des instances de services, notamment leur **initialisation**, leur **traitement** et leur **fermeture**.
Cela permet de centraliser et simplifier l'organisation des différents services nécessaires à l'application.

### Principales fonctionnalités

- **Gestion des services :** L'application permet d'enregistrer, d'initialiser, de traiter et de libérer les services
  dans une structure centralisée.
- **Contrôle du cycle de vie :** La classe gère les différentes étapes du cycle de vie de l'application (exécution,
  pause, fin).
- **Personnalisation :** Les comportements de l'application (nom, réglages de débogage, boucle principale, etc.) peuvent
  être configurés pour répondre à différents besoins.

## Cas d'utilisation (Use Cases)

### 1. Initialisation de l'application

1. L'utilisateur démarre une application basée sur la classe `App`.
2. Les arguments de configuration sont transmis à l'application.
3. Tous les services enregistrés sont initialisés dans l'ordre de priorité défini.

### 2. Exécution de l'application (Boucle principale)

1. L'application entre dans une boucle principale pour exécuter les services.
2. Les services sont traités successivement jusqu'à ce qu'une condition de sortie soit remplie :
    - **Activation du signal d'arrêt (`exit`)**
    - **Nombre maximum d'itérations atteint (`maxLoopCount`)**
    - **Pause temporaire (`pause`)**

3. La gestion des erreurs ou des exceptions au cours de l'exécution s'effectue au niveau des services.

### 3. Fermeture de l'application

1. L'utilisateur ou un service signe l'arrêt du programme.
2. Tous les services actifs sont fermés et libèrent leurs ressources.
3. Les statistiques des services sont collectées en une vue consolidée.

### 4. Gestion dynamique des services

1. Ajout, modification ou suppression de services à chaud (dans certains cas d'utilisation spécifiques).
2. Les services peuvent être configurés via un mécanisme centralisé.

## Détails de configuration

### 1. Paramètres principaux

| Nom            | Type      | Valeur par défaut | Description                                                                                              |
|----------------|-----------|-------------------|----------------------------------------------------------------------------------------------------------|
| `appName`      | `String`  | `"App"`           | Nom de l'application, utilisé pour identifier l'instance.                                                |
| `exit`         | `boolean` | `false`           | Définit si l'application doit s'arrêter (`true`) ou continuer à fonctionner (`false`).                   |
| `pause`        | `boolean` | `false`           | Indique si le traitement est temporairement mis en pause.                                                |
| `maxLoopCount` | `long`    | `-1`              | Nombre maximum d'itérations de la boucle principale (valeur `-1` pour une exécution continue).           |
| `debug`        | `int`     | `0`               | Niveau de débogage : 0 (aucun débogage) à des niveaux supérieurs pour plus de détails dans les journaux. |

### 2. Gestion des services

Les services sont stockés sous forme de **Map** (clé : nom du service, valeur : instance du service). Cela permet :

- D'exécuter des appels centralisés pour les opérations (initialisation, traitement, nettoyage).
- De classer et de prioriser les services selon des critères personnalisés.

### 3. Cycle de vie

| Étape     | Description                                                                                              |
|-----------|----------------------------------------------------------------------------------------------------------|
| `init`    | Prépare les services et configure l'application à l'aide des paramètres passés en argument.              |
| `process` | Exécute la boucle principale. Elle traite chaque service tout en respectant les états de pause ou arrêt. |
| `dispose` | Libère les ressources en appelant les fermetures individuelles des services enregistrés.                 |

## Eventuelles extensions

- Ajout d'une interface graphique pour visualiser l'état et les statistiques des services.
- Gestion dynamique avancée des services : ajout ou suppression pendant l'exécution.
- Implémentation d'une API REST pour interagir avec les services à distance.

Ce document donne une vision générale des fonctionnalités et de la configuration de la classe `App`. Pour toute question
ou développement spécifique, d'autres détails pourront être ajoutés.
