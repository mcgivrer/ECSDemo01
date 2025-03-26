# **Guide de Contribution**

Merci de votre intérêt pour améliorer **Demo01App** ! Nous accueillons chaleureusement toutes les contributions : qu'il
s'agisse de signaler des bugs, d'ajouter de nouvelles fonctionnalités, d'améliorer la documentation, ou d'optimiser le
code.
Suivez ce guide pour participer efficacement au projet et respecter les normes de collaboration.

## **1. Avant de Commencer**

### **1.1 Lisez la Documentation**

Avant de contribuer, prenez un moment pour consulter la [Documentation principale](index.md). Familiarisez-vous avec
l'objectif du projet, son architecture et ses fonctionnalités.

### **1.2 Vérifiez les Problèmes Existants**

Avant de soumettre un problème ou de travailler sur une fonctionnalité, passez en revue les **issues ouvertes** sur
notre [GitHub](https://github.com/your-repo/demo01app/issues). Cela évite les doublons et permet de mieux coordonner les
efforts.

### **1.3 Communication**

Si vous avez une idée importante ou une proposition de modification significative, ouvrez une **issue GitHub** pour en
discuter avant de commencer à coder.

## **2. Comment Contribuer**

### **2.1 Signaler un Bug**

Si vous trouvez un bug :

1. Vérifiez si le bug n’a pas déjà été signalé dans les issues existantes.
2. Ajoutez une nouvelle **issue** en incluant :
    - Une description claire du problème.
    - Les étapes pour reproduire le bug.
    - La configuration utilisée (version Java, OS, etc.).

3. Suggérez une solution si possible.

### **2.2 Proposer une Nouvelle Fonctionnalité**

Pour proposer une fonctionnalité :

1. Vérifiez si une idée similaire existe déjà dans les **issues** ou la feuille de route.
2. Ouvrez une **issue** et décrivez :
    - Votre idée et son objectif.
    - Un exemple d'utilisation.
    - L'impact potentiel sur le projet.

### **2.3 Soumettre Votre Code**

Pour contribuer au code :

1. **Fork** le dépôt [Demo01App](https://github.com/your-repo/demo01app) sur GitHub.
2. Clonez votre fork localement :

``` bash
   git clone https://github.com/votre-utilisateur/demo01app.git
```

1. Créez une nouvelle branche décrivant votre contribution :

``` bash
   git checkout -b ajout-feature-ou-correction-bug
```

1. Ajoutez vos modifications dans le code.
2. Assurez-vous de respecter les **standards de codage** :
    - Indentation correcte.
    - Utilisation de commentaires pour expliquer le code complexe.
    - Séparation claire des responsabilités dans le code.

3. Testez vos modifications si possible.
4. Ajoutez un commit :

``` bash
   git add .
   git commit -m "Description claire de vos modifications"
```

1. Poussez votre branche sur votre fork :

``` bash
   git push origin ajout-feature-ou-correction-bug
```

1. Créez une **pull request (PR)** depuis votre branche forkée.

## **3. Processus de Revue de Code**

- Une fois votre **pull request** soumise, un mainteneur du projet la passera en revue.
- Vous pourriez recevoir des commentaires vous demandant d’apporter des modifications.
- Une fois approuvée, votre PR sera fusionnée dans la branche `main`.

## **4. Bonnes Pratiques**

### **4.1 Écriture de Commits**

Suivez ces conseils pour écrire des messages de commit clairs et utiles :

- Utilisez l’impératif pour décrire l’action : _"Ajoute la gestion des scènes"_, _"Corrige le bug de rendu"_.
- Limitez les lignes de description à 72 caractères.
- Fournissez du contexte si nécessaire dans le corps du message.

### **4.2 Tests**

- Si vous ajoutez ou modifiez une fonctionnalité, incluez des **tests unitaires** et vérifiez qu'ils passent.
- Si vous corrigez un bug, ajoutez un test pour confirmer que le problème est résolu.

### **4.3 Suivez le Style du Projet**

- Consultez les fichiers existants pour comprendre les conventions utilisées.
- Respectez la structure du projet.

## **5. Ressources Utiles**

- [Guide GitHub : Fork un dépôt](https://docs.github.com/en/get-started/quickstart/fork-a-repo)
- [Introduction à Git](https://git-scm.com/doc)
- [Documentation officielle de Java](https://docs.oracle.com/en/java/)

## **6. Contact**

Si vous avez des questions ou besoin d’aide, contactez-nous via les issues sur GitHub ou
par [e-mail](mailto:frederic.delorme@gmail.com).
En suivant ces étapes, vous aiderez à garder le projet organisé tout en contribuant de manière significative. Nous avons
hâte de collaborer avec vous !

### **Merci pour votre contribution !**

Ce fichier `contribution.md` fournit un cadre clair et détaillé pour que les contributeurs puissent participer
efficacement et en connaissance de cause. Il peut être enrichi ou modifié en fonction des besoins de votre projet.
