Missing Functionality
- Options menu w/ audio controls & control rebinding: Was fairly low-priority for me, and I didn't have the time to fully implement it, so it was scrapped. This means controls are not rebindable (hope WAD+Shift isn't too awkward for you), and audio cannot be changed in game (you'll have to use Windows' built-in Volume Mixer for that)
- Track selection menu does not show personal bests: Low priorty thing that I didn't end up doing
- No pause menu: I didn't promise this, but it's also a feature that I really should have added. Guess who didn't add this feature (sorry)

"Bro if you want to leave just finish the level" /s.

Unplanned Functionality
- This wasn't necessarily "unplanned," more "I didn't consider the possibility but I guess it's here now," but both the soundtrack and levels are technically "moddable." This means you can add your own music and add your own tracks if you so desire. Note that tracks have to have a unique ID. If two tracks have the same ID, the one that was processed first (alphabetically) will be kept.

Bugs & Other Issues
- Collisions may not perform as expected under certain conditions (such as high player speeds & lag). This can happen when the player goes through a given block and intersects another block that theoretically should not be collided with. I upped the game's FPS to help with this, but you will likely still encounter some interesting collisions
- The game MAY stutter and freeze to the point of unplayability. For whatever reason I kept encountering stuttering and freezing when I played my own game, but other people (i.e. Daniel Su) said that it wasn't lagging all that much. If you go into settings.save in the save folder, there is a line that tells the game whether or not to enable OpenGL rendering, which helped the lag for me. Do note that the OpenGL mode does result in some minor graphical glitches.
- In general, the game reacts very negatively if a track file or save file is not formatted 100% correctly. For the most part, I've implemented checks that make the game ignore the track file rather than throw an exception. 

Notes
- You do not HAVE to play through the levels sequentially, and they are all unlocked at the beginning of the game. This is not recommended, though, because of point #2.
- The base game levels are laid out in order of increasing complexity (which mostly correlates with difficulty) from series A-E. However, there are some caveats to this trend:
    - Tracks within each series are not always laid out in increasing complexity/difficulty order (e.g. C02 is easier than C04)
    - The E series of tracks is a series of "meme tracks" which are designed to go against literally every core tenet of good level design for these kinds of games. I wouln't really recommend playing them, at least until the end of your playthroughs
- Most of the menu designs were changed from the concepts, though functionality is otherwise the same (except where explicitly mentioned)
- I did my best to mitigate this, but there is still a non-zero chance of seeing an exception in the console (usually stemming from invalid .track or .save formatting). Unless the game crashes or doesn't even boot, these exceptions will not interrupt the game.