Missing Functionality
- Options menu w/ audio controls & control rebinding: Was fairly low-priority for me, and I didn't have the time to fully implement it, so it was scrapped. This means controls are not rebindable (hope WAD+Shift isn't too awkward for you), and audio cannot be changed in game (you'll have to use Windows' built-in Volume Mixer for that)
- Track selection menu does not show personal bests: Low priorty thing that I didn't end up doing
- No pause menu: I didn't promise this, but it's also a feature that I really should have added. In the end however, my code would have required some restructuring, which would have taken time that I didn't have. 

"Bro if you want to leave just finish the level" /s.

Unplanned Functionality
- This wasn't necessarily "unplanned," more "I didn't consider the possibility but I guess it's here now," but both the soundtrack and levels are technically "moddable." This means you can add your own music and add your own tracks if you so desire.

Bugs & Other Issues
- Collisions may not perform as expected under certain conditions (such as high player speeds & lag). This can happen when the player goes through a given block and intersects another block that theoretically should not be collided with. The best way to fix this would be to increase the game's update speed, but that creates enough issues that I decided to prioritize it below everything else (which is why it never got fixed)
- The game MAY stutter and freeze to the point of unplayability. For whatever reason I kept encountering stuttering and freezing when I played my own game, but other people (i.e. Daniel Su) said that it wasn't lagging all that much. I'm honestly so frustrated by this but I have no clue how to fix it
- In general, the game reacts very negatively if a track file or save file is not formatted 100% correctly. For the most part, I've implemented checks that make the game ignore the track file rather than throw an exception. 

Notes
- You do not HAVE to play through the levels sequentially, and they are all unlocked at the beginning of the game. This is not recommended, though, because of point #2.
- Theoretically, the base game levels are laid out in order of increasing complexity (which mostly correlates with difficulty). Each track is grouped into the A-E series, which get progressively more complicated with each level. However, levels within the series do not necessarily follow this trend (e.g. E03 is, in my opinion, much harder than E05).
- Most of the menu designs were changed from the concepts, though functionality is otherwise the same (except where explicitly mentioned)
- I did my best to mitigate this, but there is still a non-zero chance of seeing an exception in the console (usually stemming from invalid .track or .save formatting). Unless the game crashes or doesn't even boot, these exceptions will not interrupt the game.