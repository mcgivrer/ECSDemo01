# Classe `Entity`

## Description

La classe **`Entity`** représente un élément de base dans un système qui utilise une gestion par **entités et composants
**. Elle est conçue pour être extensible et modulable grâce à ces fonctionnalités principales :

- **Gestion unique** : Chaque entité a un identifiant unique (`id`, basé sur un compteur incrémental, et un UUID
  aléatoire).
- **Hiérarchie** : Une entité peut contenir des **enfants** pour former une structure arborescente.
- **Composants** : Les entités peuvent être enrichies via des composants, permettant d'ajouter dynamiquement des
  fonctionnalités sans modifier directement la classe entité.
- **Cycle de vie** : Une entité est activée ou désactivée en fonction de sa durée de vie (`lifeTime` et `duration`).

### Architecture de la classe

- **ID unique** :
    - Chaque entité est identifiée par un identifiant incrémental (`id`) et un identifiant universel (`UUID`).

- **Enfants et racine** :
    - Les entités peuvent contenir une liste d'autres entités (enfants), permettant de créer une hiérarchie.
    - Une entité racine (`root`) représente le point de départ de cette arborescence.

- **Composants** :
    - Les entités embarquent des composants dynamiques accessibles par type.

- **Cycle de vie limité** :
    - Une entité a une durée de vie définie par `duration`. Après expiration, elle peut être automatiquement désactivée.

- **Activité** :
    - Les entités peuvent être actives ou inactives (`setActive`) et disposent d'une méthode de mise à jour (
      `update(elapsed)`), essentielle dans un système temps-réel.

## Utilisation

### Principaux cas d'utilisation

1. **Représentation d'objets dynamiques**
    - Utilisé pour représenter des objets génériques dans une application (éléments de jeu, UI, entités physiques,
      etc.).
    - Ces entités peuvent avoir un comportement évolutif en s'appuyant sur des composants.

2. **Hiérarchies d'entités**
    - Grâce au système d’enfants (`children`), des entités peuvent être regroupées logiquement (ex. un joueur et ses
      familiers dans un jeu ou des groupes dans une interface utilisateur).

3. **Cycle de vie des entités dynamiques**
    - Les entités sont activées/désactivées en fonction de leur durée de vie (`duration`) ou des événements.

4. **Ajout de composants pour une modularité**
    - Chaque entité peut contenir une liste de composants permettant d'ajouter dynamiquement des **capacités spécifiques
      ** (physique, animation, intelligence artificielle).

### Exemple de flux d'utilisation

1. **Créer des entités** :

``` java
   Entity player = new Entity("Player");
   Entity enemy = new Entity("Enemy");
```

1. **Créer une hiérarchie** :

``` java
   Entity root = new Entity("Root");
   root.add(player);
   root.add(enemy);
```

1. **Ajout de composants** :
    - Un composant pourrait être une fonctionnalité de déplacement ou de physique.

``` java
   player.add(new PhysicsComponent());
   enemy.add(new AIComponent());
```

1. **Mise à jour et cycle de vie** :
    - Sur chaque `tick` ou frame de simulation, l’état des entités est mis à jour :

``` java
   double elapsed = 0.016; // Temps écoulé en secondes
   player.update(elapsed);
```

# Service de Gestion des Entités - `EntityManagerService`

## Description

La classe **`EntityManagerService`** est un service responsable de la gestion centralisée des instances d'**entités** (
`Entity`). Elle permet :

- De **créer, ajouter, récupérer ou supprimer des entités** de manière thread-safe.
- De gérer le **cycle de vie et l’état des entités** au sein de l'application.
- De fournir des **statistiques** sur les entités actives et inactives.
- De faciliter la **manipulation collective** d'entités à travers des outils d'accès globaux (par nom ou par liste).

### Architecture

1. **Collection sécurisée** :
    - Les entités sont gérées via une **`ConcurrentHashMap`** pour garantir un accès sécurisé dans un environnement
      multi-threadé.
    - Elles sont indexées par leur nom unique (`Entity#getName`).

2. **Statistiques** :
    - Le service offre des statistiques de base telles que :
        - Nombre total d'entités.
        - Nombre d'entités actives.

3. **Intégration avec le cycle de vie de l'application** :
    - L’`EntityManagerService` est intégré au cycle standard des services :
        - **Initialisation (`init`)**
        - **Traitement récurrent (`process`)**
        - **Nettoyage (`dispose`)**

4. **Mise à jour collective** :
    - Pendant son passage dans la boucle principale de l'application (`process`), le service gère les mises à jour des
      entités.

## Utilisation

### Principaux cas d'utilisation

1. **Gestion centralisée des entités**
    - Toutes les entités sont ajoutées au service pour un gestionnaire unique facilitant leur manipulation.

2. **Recherche et récupération d'entités**
    - Une entité peut être retrouvée par son nom (`Entity#getName`).

3. **Utilisation dans le cycle appliqué à tous les systèmes**
    - Pendant l'exécution, chaque frame, les entités peuvent être mises à jour selon leurs propriétés (composants ou
      durée de vie).

4. **Surveillance des entités**
    - Collecte de statistiques sur les entités actives, leur nombre total, etc.

### Exemple d'utilisation

1. **Ajout d’une entité** :

``` java
   EntityManagerService manager = new EntityManagerService(app);
   Entity player = new Entity("Player");
   manager.add(player);
```

1. **Récupération par nom** :

``` java
   Entity retrievedPlayer = manager.get("Player");
   if (retrievedPlayer != null) {
       System.out.println("Entity found: " + retrievedPlayer.getName());
   }
```

1. **Mise à jour globale** (dans le méthode `process`) :

``` java
   manager.process(app); // Met à jour toutes les entités gérées
```

1. **Statistiques** :
    - Obtenir le nombre d'entités actives et totales :

``` java
   Map<String, Object> stats = manager.getStats();
   System.out.println("Nombre d'entités actives : " + stats.get("service.entity.manager.counter.active"));
   System.out.println("Nombre total d'entités : " + stats.get("service.entity.manager.counter.entities"));
```

## Relation entre `Entity` et `EntityManagerService`

1. **Ajout et gestion centralisée** :
    - Les entités créées sont ajoutées dans le `EntityManagerService` pour qu'elles soient accessibles et gérables par
      d'autres composants ou services.

2. **Cycle de vie commun** :
    - Le service gère le cycle de vie collectif : mise à jour, suppression, désactivation.

3. **Statistiques et surveillance** :
    - Grâce au service, il est plus facile de surveiller les états des entités et de gérer leurs interactions.

## Conclusion

La classe **`Entity`** et le **`EntityManagerService`** forment une architecture robuste et extensible pour gérer des
éléments dynamiques dans une application. Ce modèle est adapté aux moteurs de jeu ou à tout système nécessitant la
gestion d’objets complexes avec des relations hiérarchiques et des composants modulaires. Le service centralise la
gestion des entités, tandis que les entités elles-mêmes sont conçues pour être flexibles et autonomes.
