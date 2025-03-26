# Ajout de la classe `Node` et son Impact dans l'Architecture

La classe `Node` introduit un **modèle de structure hiérarchique** dans le framework. Elle permet de représenter des
entités sous forme d'arbres, chaque nœud ayant potentiellement des parents et des enfants. Cette approche offre une
gestion plus organisée des entités dans des scènes complexes tout en facilitant des opérations telles que la gestion des
relations parent-enfant, la propagation d'états (comme l'activation/désactivation) ou la mise à jour d'ensembles d'
entités.

---

## 1. **Classe `Node` : Présentation**

La classe `Node` agit comme une abstraction basique pour créer des systèmes hiérarchiques, en offrant des
fonctionnalités pour :

- Assigner un **ID unique** généré automatiquement (via un identifiant incrémental ou un UUID).
- Maintenir une liste de **nœuds enfants**.
- Gérer un état d'**activation** à propager dans la hiérarchie des nœuds.
- Cibler un **nœud racine** (`root`) permettant de centraliser la structure (si besoin, pour des opérations globales).

---

## 2. **Utilisation dans `Entity`**

### 2.1. Héritage depuis `Node`

La classe `Entity` hérite de `Node`, intégrant ainsi les capacités hiérarchiques directement dans le système des
entités.

### 2.2. Effets concrets :

- **Relations parent-enfant** : Une entité peut désormais avoir des enfants ou être associée à un parent. Ceci facilite
  la division logique des entités en groupes (par exemple, personnage et ses équipements).
- **Propagation d'états** : Les états d'activation ou de désactivation s'étendent aux enfants, ce qui permet une gestion
  uniforme (par exemple, désactiver le personnage désactivera aussi son équipement).
- **Écosystème centralisé via la racine** : La racine (`root`) peut supporter des opérations globales comme le rendu, le
  traitement physique, ou la recherche d'entités spécifiques.

### 2.3. Nouvelles capacités d'`Entity` :

- La gestion améliorée des relations hiérarchiques associée aux enfants facilite des systèmes comme des groupes
  d'ennemis, où un parent pourrait être utilisé pour déplacer ou contrôler tout un ensemble d'entités.
- Lors des mises à jour (`update`) ou rendus, les enfants peuvent être automatiquement inclus dans le processus sans
  intervention explicite pour chaque entité.

---

## 3. **Utilisation dans `AbstractScene`**

### 3.1. Héritage depuis `Node`

La classe `AbstractScene` hérite également de `Node`, permettant :

- Une **hiérarchie des scènes et leurs sous-scènes** si nécessaire.
- Une **gestion uniformisée** des scènes et des entités depuis une même structure arborescente.

### 3.2. Organisation dans une scène :

- Les **entités associées à une scène peuvent être enregistrées en tant qu'enfants**. Cela crée une hiérarchie propre à
  chaque scène et simplifie les recherches (par exemple, obtenir tous les objets actifs d'une scène).
- L'état actif ou inactif d'une scène peut automatiquement se propager aux entités ou sous-scènes associées.

### 3.3. Accès centralisé :

Par l'intermédiaire de la méthode statique `Node.setRoot`, on peut définir la scène active comme racine, offrant un
accès centralisé pour des parcours globaux des objets actifs ou pour coordonner différents services.

---

## 4. **Impact dans les services**

### 4.1. PhysicEngineService

Le `PhysicEngineService` est responsable de la gestion des interactions physiques. L'introduction de `Node` impacte ce
service de manière significative :

**Avantages intégrés grâce aux hiérarchies :**

- **Gestion globale des entités** :
    - Le service peut parcourir l'ensemble des entités physiques à l'aide de l'arbre de nœuds, en partant de la racine (
      `root`).
    - Les entités désactivées dans l'arbre sont automatiquement ignorées, réduisant ainsi les efforts de filtrage.
- **Gestion des sous-ensembles** :
    - Si des entités sont organisées en groupes, il est possible de gérer certains ensembles indépendamment, tout en
      conservant la structure globale.
    - Par exemple, des groupes d'entités comme des plateformes mobiles ou des grappes d'ennemis.

**Modifications dans le service :**

- Le moteur peut utiliser l’appel à `getChildren()` sur des nœuds pour traiter leurs entités descendantes de façon
  récursive.
- Les entités désactivées (`!isActive()`) sont naturellement exclues du calcul physique, ce qui améliore la performance
  dans les scènes complexes.

---

### 4.2. RenderingService

Le `RenderingService`, qui s'occupe de la gestion graphique, profite également de la hiérarchie introduite via `Node`.

**Avantages de la structure `Node` dans le rendu :**

- **Regroupement des entités logiques** : Le rendu des entités peut facilement être organisé en groupes, comme des
  couches ou des sous-sections dans une scène.
    - Par exemple, afficher tous les enfants d'une entité "parent" avant de rendre d'autres entités.
- **Respect des états actifs** :
    - Le service parcourt uniquement les nœuds actifs, optimisant ainsi le pipeline de rendu.
- **Parcours simplifié** : En partant de la racine (`Node.root`), le service peut itérer sur toutes les entités
  associées au rendu.

**Modifications dans le service :**

- Le `renderingList` peut être alimenté directement depuis l'arbre des nœuds, en collectant toutes les entités qui
  possèdent un `GraphicComponent`.
- Les effets graphiques liés à plusieurs nœuds (comme une transparence sur un groupe) peuvent être appliqués
  uniformément à travers les enfants d'un même parent.

---

## 5. **Résumé des impacts globaux**

L'ajout de `Node` et son intégration dans `Entity` et `AbstractScene` transforme le fonctionnement du framework en
instaurant une gestion hiérarchique des objets. Voici un récapitulatif des bénéfices majeurs et des changements
générés :

### **Avantages fonctionnels :**

1. **Relation parent-enfant** : Simplifie le regroupement logique des entités pour des interactions physiques ou
   visuelles coordonnées.
2. **Propagation automatique des états** : Les propriétés comme l'activation ou la désactivation se propagent dans la
   hiérarchie, réduisant le besoin de gestion explicite.
3. **Parcours récursif des entités** : Une structure uniforme permet de naviguer facilement dans la scène pour appliquer
   des mises à jour, des calculs physiques ou des rendus.
4. **Support pour des systèmes complexes :**
    - Gestion d’ensembles d'entités (groupes).
    - Simplicité pour les interactions entre couches dans une scène.

### **Impact sur les services :**

- **PhysicEngineService** :
    - Amélioration du traitement physique en excluant naturellement les entités désactivées.
    - Gestion structurée des sous-groupes d'entités.
- **RenderingService** :
    - Pipeline de rendu organisé en hiérarchie.
    - Rendu optimisé avec exclusion des nœuds inactifs et traitement uniforme de groupes logiques.

### Diagramme UML des classes

```plantuml
@startuml
' Définition des classes

class Node {
    - static long index
    - static Node root
    - long id
    - UUID uuid
    - String name
    - boolean active
    - List<Node> children
    --
    + Node()
    + static setRoot(Node r)
    + long getId()
    + UUID getUUID()
    + String getName()
    + Node add(Node child) : Node
    + Collection<Node> getChildren()
    + Node setActive(boolean a)
    + boolean isActive()
}

class Entity {
    - List<Component> components
    - long duration
    - long lifeTime
    --
    + Entity()
    + Entity(String name)
    + Entity add(Component c) : Entity
    + void update(double elapsed)
    + <T extends Component> T getComponent(Class<? extends Component> class1)
    + List<Component> getComponents()
    + boolean containsComponent(Class<? extends Component> class1)
}

class AbstractScene {
    - App app
    - Camera activeCamera
    - String requestNextScene
    --
    '#constructor AbstractScene(App app, String name)
    + abstract void init(App app)
    + Collection<Entity> getEntities()
    + void dispose(App app)
    + Camera getCamera()
    + String requestChange()
}

class Component {
    # abstract class to act as a base for custom components
}

class App {
    # Represents the main application context
}

class Camera {
    # Represents the active camera
}

class UUID {
    # Representation of universally unique identifier
}

Node <|-- Entity : "extends"
AbstractScene <|-- Node : "extends"
Entity *-- Component : "contains"
AbstractScene *-- Entity : "contains"
Node "1" *-- "*" Node : "children"
@enduml
```


### **Extensions possibles :**

- Création de systèmes supplémentaires, comme un **gestionnaire d'événements hiérarchique** basé sur `Node`, pour
  propager les événements (ex. clic, collision) depuis un parent à ses enfants ou inversement.
- Ajout de propriétés globales sur les parents affectant uniformément les enfants (par exemple, une transformation ou un
  effet visuel).

En conclusion, l’introduction de la classe `Node` représente une amélioration significative, qui renforce la modularité,
l’organisation et la performance globale des systèmes de gestion d’entités et de scènes.
