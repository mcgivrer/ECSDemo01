# Le Moteur Physique

Le service **`PhysicEngineService`** gère et met à jour les propriétés physiques des entités dans une simulation. Il
s'intègre dans un environnement orienté entités et interagit principalement avec les classes **`Entity`**, **`Material`
** et **`World`** pour simuler des mécanismes physiques comme les forces, la gravité et les collisions.

## 1. Fonctionnement de **`PhysicEngineService`**

**`PhysicEngineService`** est responsable de :

- **Application des lois physiques** : Simule les forces gravitationnelles, les collisions et la dynamique des entités.
- **Mise à jour des entités** : Calcule et applique les transformations (position, vélocité) pour chaque entité en
  fonction du temps écoulé.
- **Gestion des interactions** : En collaboration avec la classe **`World`**, il applique les forces globales (par
  exemple : gravité) et vérifie si les entités respectent les contraintes (comme les limites de l’aire de jeu).

## 2. Résumé des interactions

### a) Interaction avec **`Entity`**

Les **entités (`Entity`)** représentent les objets physiques manipulés par le moteur. Chaque entité :

- Possède des **composants physiques** (`PhysicComponent`) qui stockent des propriétés telles que la position, la
  taille, la vélocité, les forces appliquées et leur masse issue de la densité du matériau utilisé.
- Peut être mise à jour dynamiquement par le moteur physique pour ajuster ses attributs en fonction des contraintes ou
  des forces.

Le moteur physique extrait les composants physiques `PhysicComponent` de chaque entité pour appliquer des calculs
Newtoniens, puis met à jour leur état.

### b) Interaction avec **`Material`**

La classe **`Material`** permet de caractériser les propriétés physiques des entités :

- **Densité** : Affecte la masse d'une entité.
- **Élasticité** : Détermine la réponse à une collision (rebond).
- **Rugosité** : Définit les interactions de friction entre deux surfaces.

Les propriétés du matériau influencent directement les calculs physiques réalisés par le moteur (par exemple, les
collisions et les forces).

### c) Interaction avec **`World`**

**`World`** représente l'environnement simulé par le moteur physique :

- Définit une **aire de jeu** (limites spatiales) où les entités peuvent exister.
- Applique une **gravité globale** sur les entités via un vecteur de force défini.
- Stocke les contraintes, comme les murs ou les limites, qui influencent le comportement des entités.

Le moteur physique utilise la gravité définie dans le `World` et s'assure que les entités restent dans cette "zone de
jeu" via des vérifications de bordures.

## Exemple de fonctionnement logique

1. **Initialisation** :
    - Le **`PhysicEngineService`** récupère la liste des entités via le **`EntityManagerService`**.
    - Récupère les propriétés globales du **`World`**, comme la gravité.

2. **Étape de mise à jour (`process`)** :
    - Pour chaque entité :
        - Applique les forces externes et internes stockées dans son `PhysicComponent` (gravité, collisions).
        - Met à jour la position et la vélocité de l'entité en fonction des calculs Newtoniens.
        - Vérifie les collisions avec les limites du `World` (par exemple : contact avec le sol ou impact avec un mur).

    - Recalcule les interactions entre entités (par exemple : collisions entre deux objets).

3. **Résultat** :
    - Une simulation fluide basée sur la dynamique des corps sous l'influence de forces physiques et des contraintes
      environnementales.

## Diagramme UML en PlantUML

Voici un diagramme UML montrant les relations entre `PhysicEngineService`, `Entity`, `Material` et `World` :

``` plantuml
@startuml
class PhysicEngineService {
    - EntityManagerService eMgr
    - World world
    - long currentTime
    - int nbUpdatedObjects
    + void process(App app)
}

class Entity {
    - long id
    - String name
    - List<Component> components
    + Entity add(Component c)
    + Collection<Entity> getChildren()
    + long getId()
}

class World {
    - Rectangle2D playArea
    - Vector2d gravity
    + World setGravity(Vector2d g)
    + World setPlayArea(Rectangle2D playArea)
    + Rectangle2D getPlayArea()
    + Vector2d getGravity()
}

class Material {
    - String name
    - double density
    - double elasticity
    - double roughness
    + void setDensity(double d)
    + void setElasticity(double e)
    + void setRoughness(double r)
    + double getDensity()
    + double getElasticity()
    + double getRoughness()
}

class PhysicComponent {
    - Vector2d position
    - Vector2d velocity
    - double mass
    - List<Vector2d> forces
    + Vector2d getPosition()
    + void add(Vector2d force)
}

PhysicEngineService --> Entity : "met à jour"
PhysicEngineService --> World : "applique gravité et limites"
Entity --> PhysicComponent : "comporte un"
Entity --> Material : "peut utiliser"
World --> PhysicComponent : "interagit avec"
Material --> PhysicComponent : "définit les propriétés"
@enduml
```

## Explication du diagramme UML

1. **`PhysicEngineService`** est responsable de :
    - Utiliser les entités (`Entity`) pour appliquer des mises à jour physiques.
    - Appliquer la gravité et les limites définies dans le `World` aux entités.

2. **`Entity`** :
    - Contient des composants physiques (`PhysicComponent`) pour stocker position, forces et masse.
    - Peut utiliser un matériau (`Material`) pour définir des propriétés influençant les calculs physiques.

3. **`World`** :
    - Définit le contexte global avec la gravité (`Vector2d`) et une aire de jeu (`Rectangle2D`).
    - Interagit directement avec le composant physique des entités pour s'assurer qu'elles respectent les limites et
      sont impactées par les règles.

4. **`Material`** :
    - Représente les propriétés physiques utilisées par les calculs effectués dans les composants physiques (
      `PhysicComponent`).

## Conclusion

**`PhysicEngineService`** agit comme une boucle de simulation physique qui met à jour les entités en fonction de leurs
composants physiques (`PhysicComponent`), des propriétés des matériaux et des contraintes définies par le monde
environnant (`World`). En combinant ces éléments, il permet de créer une simulation cohérente et réaliste.
