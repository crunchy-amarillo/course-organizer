This is an example project create for the course "Mobile Software Engineering" of the IU.

It's my first android project - so don't blame me for mistakes or uncommon code. ;-)

# Test users

The application can be used by multiple users. There are the following default users:

| username | password | 
| ----------- | ----------- |
| user1 | pw1 | 
| user2 | pw2 |

If you want more users, you have to adjust `User.populateData()` and reinstall the application.

# List view

* use add button (bottom, right) to add new items
* swipe to delete

# Exports

## CSV

You can export the data as CSV for:

* courses
* learning units

You will find the export button within the menu.

## PDF

You can "export" (aka do a screenshot) of every app page. You can find the export (and mail) button
within the burger menu.

# Course list import

The import is available for courses. You can find the import within the burger menu at the top of
the app page.

* necessary format: csv
* header row with column titles is necessary
* importable columns: title (string), description (string), session (integer)
* separator: ,
* only working for files in external storage (not files from download or media)