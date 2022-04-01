import { handleHttpErrors } from "../fetchUtils.js"
import { SERVER } from "../settings.js"

const SERVER_URL = SERVER + "/api/quotes"

export function page4Handlers() {
  document.getElementById("btn-find").onclick = findQuote
  document.getElementById("btn-edit").onclick = editQuote
  document.getElementById("btn-delete").onclick = deleteQuote
}

async function findQuote() {
  document.getElementById("error").innerText = ""
  const id = getIdFromInputField()
  try {
    const foundQuote = await fetch(`${SERVER_URL}/${id}`)
      .then(handleHttpErrors)

    document.getElementById("quote").value = foundQuote.quote
    document.getElementById("author").value = foundQuote.ref
  }
  catch (err) {
    document.getElementById("error").innerText = err.message
  }
}

// function findQuote() {
//   const id = getIdFromInputField()
//   fetch(`${SERVER_URL}/${id}`)
//     .then(res => {
//       if (!res.ok) {
//         throw new Error("Could not find quote (")
//       }
//       return res.json()
//     })
//     .then(foundQuote => {
//       document.getElementById("quote").value = foundQuote.quote
//       document.getElementById("author").value = foundQuote.ref
//     })
//     .catch(e => alert(e.message + " (NEVER use alerts for real)"))
// }

async function editQuote() {
  const id = getIdFromInputField()
  const editedQuote = {
    id: id
  }
  editedQuote.quote = document.getElementById("quote").value
  editedQuote.ref = document.getElementById("author").value
  try {
  await fetch(SERVER_URL + "/" + id, {
    method: "PUT",
    headers: {
      "Accept": "application/json",
      "Content-type": "application/json"
    },
    body: JSON.stringify(editedQuote)
  })
    .then(handleHttpErrors)

    clearFields();
  }
    catch(err) {
      document.getElementById("error").innerText = err.message
    }
}
async function deleteQuote() {
  const id = getIdFromInputField()
  await fetch(SERVER_URL + "/" + id, {
    method: "DELETE"
  }).then(res => {
    res.text()
  })
  clearFields()
}

function clearFields() {
  document.getElementById("quote-id").value = ""
  document.getElementById("quote").value = ""
  document.getElementById("author").value = ""
}

function getIdFromInputField() {
  const id = document.getElementById("quote-id").value
  if (id === "") {
    throw new Error("No ID Provided")
  }
  return id
}
