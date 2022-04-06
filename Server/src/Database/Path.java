package Database;

import java.util.HashMap;

// << Path Database >>
public class Path {
    // < Attributes >
    HashMap<String, String> pathDataMap = new HashMap<>();
    // psn, vsn                     = Key data
    // xpath, ypath                 = Path data
    // xop, yop, sop, xdp, ydp, dpp = Origin/Destination data
    // pia, pib, pic, pid, pie      = Passenger Index data
    // cost                         = Cost data
}