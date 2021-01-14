const router = require("express").Router();
const user = require("../../entities/User");

router.get("/", async (req, res) => {
  return res.json(await user.getAll());
});

router.get("/check/:username", async(req, res) => {
  return res.json(await user.getUserName(req.params.username));
})

router.get("/check/email/:email", async(req, res) => {
  return res.json(await user.getEmail(req.params.email));
})


router.post("/", async (req, res) => {
  const { username, email, password, avatar } = req.body;
  return res.json(
    await user.registerNewUser(username, email, password, avatar)
  );
});

router.post("/friends/", async (req, res) => {
  if (!req.session.user) {
    return res.json({ error: "Not logged in!" });
  }
  return res.json(await user.createOrAcceptFriendRequest(req.session.user.username, req.body.friendname));
});

router.delete("/friends/", async (req, res) => {
  if (!req.session.user) {
    return res.json({ error: "Not logged in!" });
  }
  return res.json(await user.deleteFriend(req.session.user.username, req.body.friendname));
});

router.get("/friends/", async (req, res) => {
  if (!req.session.user) {
    return res.json({ error: "Not logged in!" });
  }
  return res.json(await user.getFriends(req.session.user.username));
});

router.get("/nonfriends/", async (req, res) => {
  if (!req.session.user) {
    return res.json({ error: "Not logged in!" });
  }
  return res.json(await user.getNonFriends(req.session.user.username));
});

router.get("/friends/requests/", async (req, res) => {
  if (!req.session.user) {
    return res.json({ error: "Not logged in!" });
  }
  return res.json(await user.getIncomingFriendRequests(req.session.user.username));
});

router.get("/nonfriends/requests/", async (req, res) => {
  if (!req.session.user) {
    return res.json({ error: "Not logged in!" });
  }
  return res.json(await user.getOutgoingFriendRequests(req.session.user.username));
});

// router.post("/friends/", async (req, res) => {
//   if (!req.session.user) {
//     return res.json({ error: "Not logged in!" });
//   }
//   return res.json(await user.createOrAcceptFriendRequest(req.session.user.username, req.body.friendname));
// });

module.exports = router;
