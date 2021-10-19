"""
Usage: python3 dungeonMaker.py <file>
Usage: python3 dungeonMaker.py dungeons/    # you most likely want to do that

Limitations:
- cannot have more than one item per cell
- doesn't support goals

File format example (check the variable code_map below to understand)

    xpx..x
    x..x.x
    x....x
    xxxsxx

"""

import sys
import json
import os
import os.path

if len(sys.argv) != 2:
    print("usage: python3 dungeonMaker.py <file>")
    print("more documentation in the script")
    exit(1)

if not os.path.isdir("./src/main/resources/dungeons/"):
    print("./src/main/resources/dungeons/ doesn't seem to exist")
    print("hint: you should be at the root of the project when running this script")
    exit(1)

code_map = {
    "x": "wall",
    ".": None,  # empty
    "s": "zombie toast spawner",
    "p": "player",
    "e": "exit",
}


def convert_file(filename):
    with open(filename, "r") as fp:
        entities = []
        width = -1
        height = 0
        for y, line in enumerate(fp):
            height += 1
            line = line.strip()

            if width == -1:
                width = len(line)
            elif width != len(line):
                raise ValueError(
                    f"the dungeon doesn't have a consistent width (expect {width}, got {len(line)}"
                )

            for x, char in enumerate(line):
                if char not in code_map:
                    raise ValueError(f"unknown character {char!r}")
                elif code_map[char] is not None:
                    entities.append({"x": x, "y": y, "type": code_map[char]})

    name = os.path.splitext(os.path.basename(filename))[0]
    with open(
        os.path.join("./src/main/resources/dungeons/", f"{name}.json"), "w"
    ) as out:
        json.dump(
            {
                "comment": f"This file was automatically generated. Edit the {name}.txt file instead",
                "width": width,
                "height": height,
                "entities": entities,
            },
            out,
            indent=2,
        )

def convert_dir(dir):
    for filename in os.listdir(dir):
        filepath = os.path.join(dir, filename)
        if not os.path.isfile(filepath):
            print("skipping", filepath)
        else:
            print("converting", filepath)
            convert_file(filepath)

if __name__ == "__main__":
    if os.path.isdir(sys.argv[1]):
        convert_dir(sys.argv[1])
    elif os.path.isfile(sys.argv[1]):
        convert_file(sys.argv[1])
    else:
        print(f"{sys.argv[1]} doesn't seem to be a file")
