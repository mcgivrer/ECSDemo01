# Mécanisme des Services

## Description générale

Le système de "Services" utilisé dans le cadre de la classe `App` repose sur une architecture modulaire. Les services
représentent des composants autonomes qui encapsulent des fonctions ou des comportements spécifiques. Ces services sont
conçus pour être **gérés, exécutés et disposés** tout au long du cycle de vie de l'application.

### Structure

1. **Interface `Service`** :
    - Définit le contrat standard pour un service, incluant les méthodes nécessaires pour :
        - Initialiser (`init`).
        - Exécuter (`process`).
        - Prioriser (`getPriority`).
        - Nettoyer et désallouer les ressources (`dispose`).
        - Gérer les statistiques du service (`getStats`).

    - Chaque service doit protéger son cycle de vie en implémentant cette interface.

2. **Classe abstraite `AbstractService`** :
    - Fournit une implémentation par défaut de certaines fonctionnalités pour simplifier la création de nouveaux
      services.
    - Introduit un mécanisme de statistiques (via une **map thread-safe** : `ConcurrentHashMap`) pour suivre les
      métriques liées au service.
    - Associe chaque service à une instance de l'application (classe `App`), permettant au service d'interagir avec les
      autres composants.

3. **Classe `SceneManagerService`** :
    - Une implémentation spécifique de la gestion des **scènes** dans le jeu ou l'application.
    - Ce service est capable de :
        - Charger une liste de scènes à partir de la configuration.
        - Maintenir une scène active (avec possibilité de changer ou désactiver la scène active).
        - Disposer proprement des ressources des scènes lorsque le service est arrêté ou reconfiguré.

## Mécanisme de fonctionnement

Les services sont gérés par une instance de la classe `App`, qui orchestre leur cycle de vie :

1. **Enregistrement et hiérarchisation** :
    - Les services sont ajoutés à une **Map** (clé : nom du service, valeur : instance de `Service`) maintenue par la
      classe `App`.
    - Chaque service dispose d'une priorité (`getPriority`) pour déterminer son ordre d'exécution lors des phases **init
      **, **process** ou **dispose**.

2. **Initialisation (`init`)** :
    - Lors de cette phase, chaque service est configuré à partir des paramètres globaux de l'application (argument
      `args`).
    - Exemples :
        - Charger une liste de scènes (`SceneManagerService`).
        - Vérifier les dépendances nécessaires (if any).

3. **Exécution (`process`)** :
    - Les services sont activement exécutés dans une boucle principale gérée par la classe `App`.
    - Chaque service effectue ses propres actions, qui peuvent inclure des traitements, la vérification de changements
      d'état ou la réponse à des événements.

4. **Dispose (`dispose`)** :
    - Cette phase libère toutes les ressources allouées par le service (comme les scènes dans le `SceneManagerService`).
    - Elle est cruciale pour garantir la stabilité et éviter des fuites mémoire.

5. **Statistiques (`getStats`)** :
    - Chaque service peut collecter des statistiques ou des métriques spécifiques pour le suivi ou le débogage.

## Cas d'utilisation (Use Cases)

### 1. Gestion des scènes dans un jeu

- **Acteurs :** `SceneManagerService`
- **Étapes :**
    1. Charger une liste de scènes spécifiées dans la configuration (clé : `app.scenes.list`).
    2. Définir une scène par défaut qui sera activée à l'initialisation (clé : `app.scenes.default`).
    3. Pendant l'exécution :
        - Permettre la navigation entre scènes (activation et désactivation dynamique des scènes).
        - Disposer les scènes précédemment actives lorsque nécessaire.

### 2. Extensibilité de l'application

- Les développeurs peuvent ajouter ou remplacer des services en implémentant l'interface `Service`.
- Les nouveaux services pourraient inclure :
    - Gestion des entrées utilisateur.
    - Gestion des ressources multimédia.
    - Services réseau ou de sauvegarde.

### 3. Suivi des performances

- Chaque service collecte des **statistiques opérationnelles** accessibles via la méthode `getStats` :
    - Par exemple, le `SceneManagerService` pourrait suivre :
        - Le nombre de scènes chargées.
        - Le temps écoulé sur une scène donnée.
        - Les ressources actuelles utilisées.

### 4. Gestion d'un moteur de jeu évènementiel

- Basé sur un service comme le `SceneManagerService`, l'application peut réagir dynamiquement :
    - Commencer ou arrêter une nouvelle scène.
    - Anticiper des événements liés au jeu (ex. : transitions, animations, ou interactions).

## Configuration des services

Dans ce modèle, plusieurs éléments de configuration sont requis pour assurer la flexibilité et l’extensibilité des
services.

### 1. Configuration des scènes via `SceneManagerService`

- Les scènes peuvent être configurées via des **clés de configuration** dans le fichier de configuration principal :
    - **`app.scenes.list`** : Liste des classes des scènes à gérer (exemple : `com.snapgames.scenes.MainScene`).
    - **`app.scenes.default`** : Nom de la scène active par défaut.

### 2. Priorité des services

- Chaque service peut définir sa priorité grâce à la méthode `getPriority()`. Cela garantit un ordre cohérent :
    - Exemple d'ordre pour un jeu :
        - Charger un service de configuration (priorité 1).
        - Définir les services de gestion des entrées et des graphismes (priorité 2).
        - Gérer les scènes (`SceneManagerService`, priorité 3).

### 3. Intégration avec l'instance de l'application (`App`)

- Chaque service reçoit une référence de l'application parent (`App`) lors de son initialisation.
- Cela permet :
    - D'accéder à des ressources partagées.
    - De coordonner des opérations entre services.

## Conclusion

Le mécanisme des services basé sur les classes `Service`, `AbstractService` et `SceneManagerService` propose une
architecture modulaire, extensible et orientée objets. Il permet de centraliser la gestion des fonctionnalités tout en
isolant leur implémentation dans des composants indépendants. Ce design est particulièrement adapté aux systèmes
complexes tels que les moteurs de jeux ou les frameworks applicatifs.
