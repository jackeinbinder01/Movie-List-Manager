## GUI Testing

---
### Filter Pane
##### Expected Behavior:

- Apply Filters button filters the selected tab in the List Pane
- Clear Filters button removes filters from the List Pane, and removes text/options from Filter Pane fields
  (except for range filters)
- All filters maintain data when focus is lost
- Range filters that are empty reset to their respective min/max value when focus is lost
- Range filters clear when focus is gained
- The Box Office Earnings range filter formats user entered data into millions of dollars when focus is lost
- Range filters refresh to reflect max and min values when movies are removed from the selected list in the List Pane
- All filter selections clear when a new tab is selected
- When movies from the current tab in the List Pane are removed, their corresponding options are removed from dropdown
  filters
- Movies without Box Office Earnings data display N/A in the Box Office Earnings range filter
---

### List Pane
##### Expected Behavior:
- On Tab Change
  - Filters in the Filter Pane will be cleared
  - Table selection will be cleared
  - Active tab will show all records, unfiltered
  - Export List and Delete List buttons will be...
    - Disabled in source list
    - Enabled in user-defined lists
- On Table Selection
  - The selected row will be highlighted
  - Details Pane will display the selected movie's details

---

### Details Pane
##### Expected Behavior:

- Watched checkbox and MyRating box greyed out/disabled when there's no record shown on details pane.
- Greyed out watched checkbox and MyRating box should not be editable or interactable.
- Activate watched checkbox and MyRating box when records starts showing and can be interact with.
- Upon resizing the window, no item or words got out of bound
---
