**Design Explain — Guide concis pour l'équipe

Objectif
- **But :** garantir une UI harmonieuse, prévisible et facile à maintenir par plusieurs devs.
- **Quand l'utiliser :** pour toute nouvelle UI, composant, ou modification de couleurs/typo/espacements.

Principes clés
- **Tokens uniques :** toutes les couleurs, espaces, tailles et poids sont définis dans `src/styles/_variables.scss`.
- **Mixins & utilitaires :** réutiliser `src/styles/_mixins.scss` pour boutons, cards, inputs et breakpoints.
- **Composants partagés :** utiliser `src/app/shared/components/*` (ex : `app-button`, `app-card`, `app-input`) au lieu de hardcoder styles.
- **Pas de couleurs en dur :** n'ajoutez jamais de `#xxxxxx` dans les composants — utilisez `$color-*` ou les variables CSS exposées dans `src/styles/theme.scss`.
- **Accessibilité :** contrastes minimums, focus visible, tailles tactiles (>= 44px pour CTA). Toujours vérifier avec Lighthouse/axe.

Architecture et emplacements
- Tokens & styles globaux : `src/styles/_variables.scss`, `src/styles/_mixins.scss`, `src/styles/styles.scss`.
- Thème et variables CSS : `src/styles/theme.scss` (expose variables CSS pour usage JS/CSS runtime).
- Composants réutilisables : `src/app/shared/components/` (déclarés standalone ou exportés par `SharedModule`).
- Page de démonstration visuelle : `src/app/features/design-demo/` — utilisez-la pour valider visuels avant PR.

Workflow recommandé
1. Designer la solution visuelle (Figma / simple mock).
2. Choisir tokens existants : couleurs, espacements et typo. Si un token manque, proposez l'ajout dans `src/styles/_variables.scss`.
3. Implémenter via un composant partagé si le pattern est réutilisable. Sinon, créer un composant local et ouvrir une PR.
4. Ajouter une story ou capture dans `design-demo` pour QA visuelle.
5. Tests manuels : responsive (mobile/tablet/desktop), accessibilité, état focus/disabled.

Règles de nommage
- SCSS partials : `_variables.scss`, `_mixins.scss`, `_typography.scss`.
- Tokens : `$color-primary`, `$color-secondary`, `$spacing-md`, `$font-size-base`, etc.
- Mixins : `@include btn-base`, `@include card`, `@include input-base`.

Bonnes pratiques techniques
- **Utiliser `@use`** (Sass module system) et importer explicitement les partials via chemins relatifs (ex : `@use './_variables' as vars;`).
- **Composants standalone :** préférer les composants standalone (avec `standalone: true`) pour faciliter lazy-loading.
- **Éviter les dépendances croisées circulaires** entre fichiers SCSS — garder les partials concentrés dans `src/styles/`.
- **Ne pas déclarer des styles globaux non nécessaires** dans des composants, préférez des classes utilitaires ou mixins.

Design review & PR
- Chaque PR touchant styles doit :
  - inclure un screenshot / short gif montrant avant / après
  - pointer la page `design-demo` actualisée si applicable
  - mentionner les tokens ajoutés/modifiés
- Revue ciblée : designer (si présent), 1 frontend reviewer, testeur QA.

Extensibilité & theming
- Pour ajouter un thème (ex : dark), étendre `src/styles/theme.scss` et éviter d'écraser tokens existants — mappez à des CSS variables `--bg-primary`, `--text-primary`, etc.

Checklist rapide avant merge
- [ ] Utilise-t-on un token existant ?
- [ ] Si nouveau token, est-il globalement utile ? (justification dans PR)
- [ ] Tests responsive passés
- [ ] Vérification contraste / accessibilité
- [ ] Démo dans `design-demo` ajoutée ou mise à jour

Questions / contact
- Pour décisions esthétiques ou nouveaux tokens, ouvrir une issue et assigner le/des designers.

---
Fichier utile : `src/styles/_variables.scss`, `src/styles/_mixins.scss`, `src/styles/theme.scss`, `src/app/shared/shared.module.ts`, `src/app/features/design-demo/`.

