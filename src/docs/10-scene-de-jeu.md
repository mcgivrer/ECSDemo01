# La scène de jeu `PlayScene`

La classe `PlayScene` représente une scène spécifique de jeu dans l'application. Elle implémente la logique principale
d'un niveau de jeu, y compris la gestion des entités, des interactions utilisateur (clavier) et de la configuration de
la scène. Elle étend la classe abstraite `AbstractScene`, tirant parti des fonctionnalités de gestion des entités et de
transitions de scène, et implémente l'interface `InputListener` pour capturer les événements clavier.

---

## 1. **Classe `PlayScene` : Vue d'ensemble**

La `PlayScene` est conçue pour :

1. Initialiser et enregistrer les entités spécifiques du niveau, telles que les personnages, les ennemis, les affichages
   de score et les grilles.
2. Gérer la caméra pour suivre et rendre le niveau.
3. Traiter les entrées utilisateur en temps réel, comme les actions du joueur via le clavier.
4. Mettre à jour les entités et appliquer les lois physiques pour simuler un comportement interactif.

---

## 2. **Constructeur**

```java
public PlayScene(App app, String name)
```

### Description :

- Ce constructeur initialise une nouvelle instance de `PlayScene`.
- Il appelle le constructeur de la classe parente `AbstractScene` pour configurer la scène avec une instance de
  l'application (`App`) et un nom unique (`name`).

### Paramètres :

- `App app` : Instance de l'application principale fournissant les services et ressources partagés.
- `String name` : Nom unique de la scène permettant son identification.

---

## 3. **Méthodes principales**

### 3.1 `void create(App app)`

La méthode `create` est appelée pour initialiser les entités et autres éléments nécessaires à la scène.

#### Description :

- Crée plusieurs entités spécifiques pour le niveau, notamment :
    - **Le joueur** : un personnage contrôlé par l'utilisateur.
    - **L'affichage du score** : des composants visuels affichant des informations comme les points ou l'énergie
      restante.
    - **Un écran de grille** : utilisé pour organiser la disposition du niveau ou pour des aspects de débogage.
    - **Des ennemis** : générés dynamiquement dans la scène à l'aide de la méthode interne `generateBouncingEnemies`.
- Configure la caméra pour suivre un segment spécifique de la scène, souvent le joueur.
- S'appuie sur l'instance de `App` pour accéder aux services nécessaires à la configuration (comme les moteurs d'entrée
  et de physique).

#### Points importants :

- Les entités sont équipées de composants personnalisés (comme les composants graphiques ou physiques) pour définir leur
  comportement.
- Cette étape est cruciale car elle configure la disposition initiale du niveau.

---

### 3.2 `void update(App app)`

#### Description :

La méthode `update` est responsable de l'évolution dynamique de la scène. Elle est appelée régulièrement (par exemple, à
chaque cycle d'une boucle de jeu).

#### Fonctionnalités clés :

- Vérifie l'état des entrées clavier, par exemple pour détecter si une touche est pressée.
- Applique une force ou une interaction directe au joueur si des touches spécifiques (comme les flèches directionnelles)
  sont activées.
- Met à jour l'état des entités, en appliquant les règles physiques configurées dans leurs **composants physiques**.
- Interaction avec le service de moteur physique (`PhysicEngineService`) pour effectuer des calculs de physique tels que
  la détection de collisions ou les mises à jour d'état.

#### Exemple de scénario pris en charge :

Si un utilisateur appuie sur la flèche droite, une force est appliquée pour déplacer le joueur vers la droite dans la
scène.

---

### 3.3 `void generateBouncingEnemies(int nb)`

#### Description :

Une méthode utilitaire utilisée pour ajouter un nombre prédéfini d'ennemis avec un comportement de rebond dynamique dans
le niveau.

#### Fonctionnalités clés :

- Génère `nb` ennemis avec des positions et des comportements initiaux variés.
- Configure leurs attributs, tels que la direction initiale et la couleur.
- Ces ennemis interagissent avec le moteur physique, modifiant leur trajectoire lorsqu'ils touchent des obstacles (
  rebonds).

---

### 3.4 `void onKeyReleased(App app, KeyEvent ke)`

#### Description :

Cette méthode capture les événements de relâchement des touches et déclenche les actions associées.

#### Fonctionnalités clés :

- Effectue des actions spécifiques selon la touche relâchée. Par exemple :
    - **Activation/Désactivation du mode debug** : Change le niveau de débogage affiché dans la scène.
    - **Inversion de la gravité** : Une touche peut être configurée pour inverser la gravité dans la scène (par exemple,
      déplacer les objets vers le haut plutôt que vers le bas).
- Utilise l'instance d'application pour accéder aux services nécessaires à l'exécution des commandes.

#### Exemple :

Si l'utilisateur relâche une touche de gravité, les règles physiques appliquées dans la scène sont modifiées
dynamiquement.

---

## 4. **Comportement visuel et interactif**

### Éléments visuels définis dans la scène :

- Des formes graphiques comme des rectangles et des ellipses (mais également des sprites, selon le niveau de
  personnalisation).
- Des composants pour afficher des informations essentielles au joueur, par exemple :
    - L'énergie restante sous forme de jauge.
    - Le score obtenu durant le niveau.

### Interaction utilisateur :

La `PlayScene` réagit principalement par le biais du clavier grâce à l'implémentation de l'interface `InputListener`.
Cela inclut :

1. **Déplacements** : En fonction des touches directionnelles.
2. **Actions secondaires** : Par exemple, inverser la gravité, réinitialiser le niveau, ou activer des options de
   débogage.

---

## 5. **Cycle de vie de la PlayScene**

1. **Création de la scène** :
    - La méthode `create` est appelée lors de la transition vers la `PlayScene`.
    - Les entités (joueur, ennemis, grille, etc.) sont initialisées et configurées.

2. **Mise à jour continue** :
    - À chaque étape de la boucle de jeu, la méthode `update` est invoquée :
        - Les entrées utilisateur sont interprétées.
        - Les forces physiques sont appliquées (via le moteur physique).
        - L'état des entités est ajusté (par ex. rebonds des ennemis).

3. **Interaction utilisateur en temps réel** :
    - Lorsque l'utilisateur effectue des actions (comme relâcher des touches), la méthode `onKeyReleased` génère des
      modifications immédiates dans la scène.

---

## 6. **Lien avec l'application globale**

La `PlayScene` est intégrée au système global grâce :

- Au **`SceneManagerService`** : Ce service permet de créer et de gérer la transition vers la `PlayScene` depuis une
  autre scène.
- Au **`ConfigurationService`** : Les entités et comportements spécifiques à la `PlayScene` peuvent être influencés par
  des paramètres du fichier de configuration (par ex., le nombre d'ennemis ou la résolution visuelle).

---

## 7. **Résumé des responsabilités**

### Points forts :

- La `PlayScene` est une implémentation concrète d'une scène de jeu, spécialisée pour gérer :
    - Les interactions utilisateur.
    - Les entités dynamiques.
    - Les comportements physiques.
- Elle repose sur un système modulaire (héritage de `AbstractScene`) et des services partagés de l'application (
  `PhysicEngineService`, `InputService`).

### Extensibilité :

Pour personnaliser cette scène, il est possible de :

1. Ajoutez ou modifiez des entités dans `create`.
2. Changez la logique de mise à jour du joueur et des ennemis dans `update`.
3. Définissez de nouveaux raccourcis clavier dans `onKeyReleased`.

La `PlayScene` fournit une base solide pour créer rapidement des niveaux interactifs et complexes.
